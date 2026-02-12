<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import CalendarGrid from "./CalendarGrid.vue";

const bookings = ref([]);
const loading = ref(false);
const error = ref("");

const form = reactive({
  guestName: "",
  apartmentName: "",
  startDate: "",
  endDate: "",
});

const loadBookings = async () => {
  loading.value = true;
  error.value = "";
  try {
    const response = await fetch("/api/bookings");
    if (!response.ok) {
      throw new Error("Failed to load bookings");
    }
    bookings.value = await response.json();
    if (bookings.value.length) {
      displayYear.value = new Date(
        `${bookings.value[0].startDate}T00:00:00`
      ).getFullYear();
    }
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
};

const createBooking = async () => {
  loading.value = true;
  error.value = "";
  try {
    const response = await fetch("/api/bookings", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(form),
    });
    if (!response.ok) {
      throw new Error("Failed to save booking");
    }
    form.guestName = "";
    form.apartmentName = "";
    form.startDate = "";
    form.endDate = "";
    await loadBookings();
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
};

onMounted(loadBookings);

const displayYear = ref(new Date().getFullYear());

const monthNames = [
  "Januar",
  "Februar",
  "Marz",
  "April",
  "Mai",
  "Juni",
  "Juli",
  "August",
  "September",
  "Oktober",
  "November",
  "Dezember",
];

const weekdayLabels = ["So", "Mo", "Di", "Mi", "Do", "Fr", "Sa"];

const segmentRowHeight = 24;
const segmentRowGap = 4;

const toApartmentClass = (name) =>
    `booking-segment-${String(name || "unknown")
        .toLowerCase()
        .replace(/[^a-z0-9]+/g, "-")
        .replace(/(^-|-$)/g, "")}`;

const apartmentNames = computed(() => {
  const names = bookings.value
    .map((booking) => booking.apartmentName)
    .filter(Boolean);
  return [...new Set(names)];
});

const months = computed(() => {
  const year = displayYear.value;
  const apartmentIndex = new Map(
    apartmentNames.value.map((name, index) => [name, index])
  );
  const apartmentCount = Math.max(1, apartmentNames.value.length);
  const segmentStackHeight =
    apartmentCount * segmentRowHeight + (apartmentCount - 1) * segmentRowGap;
  return Array.from({ length: 12 }, (_, index) => {
    const daysInMonth = new Date(year, index + 1, 0).getDate();
    const days = Array.from({ length: daysInMonth }, (_, dayIndex) => {
      const dayNumber = dayIndex + 1;
      const date = new Date(year, index, dayNumber);
      const key = date.toISOString().slice(0, 10);
      const weekdayIndex = date.getDay();
      return {
        date: key,
        dayNumber,
        weekdayLabel: weekdayLabels[weekdayIndex],
        isWeekend: weekdayIndex === 0 || weekdayIndex === 6,
      };
    });
    const monthStart = new Date(year, index, 1);
    const monthEnd = new Date(year, index, daysInMonth);
    const segments = [];
    for (const booking of bookings.value) {
      const start = new Date(`${booking.startDate}T00:00:00`);
      const end = new Date(`${booking.endDate}T00:00:00`);
      if (end < monthStart || start > monthEnd) {
        continue;
      }
      const segStart = start < monthStart ? monthStart : start;
      const segEnd = end > monthEnd ? monthEnd : end;
      const startIndex = segStart.getDate();
      let length;
      if (end > monthEnd) {
        length = segEnd.getDate() - segStart.getDate() + 1;
      } else {
        length = segEnd.getDate() - segStart.getDate();
      }
      const aptName = booking.apartmentName || "Unknown";
      const aptIndex = apartmentIndex.get(aptName) ?? 0;
      segments.push({
        startIndex,
        length,
        apartmentClass: toApartmentClass(aptName),
        offsetPx: aptIndex * (segmentRowHeight + segmentRowGap),
        guestName: booking.guestName,
      });
    }
    return {
      name: monthNames[index],
      days,
      segments,
      segmentStackHeight,
      colorClass: index % 2 === 0 ? "month-blue" : "month-green",
    };
  });
});
</script>

<template>
  <div class="stack">
    <header class="card">
      <h1>Moin Michael!</h1>
    </header>

    <section class="card stack">
      <div>
        <h2>Calendar</h2>
        <p class="muted">Year: {{ displayYear }}</p>
      </div>
      <CalendarGrid :months="months" :year="displayYear" />
    </section>

    <section class="card stack">
      <h2>Add booking</h2>
      <form class="grid form" @submit.prevent="createBooking">
        <label>
          Guest name
          <input v-model="form.guestName" required />
        </label>
        <label>
          Apartment
          <input v-model="form.apartmentName" required />
        </label>
        <label>
          Start date
          <input v-model="form.startDate" type="date" required />
        </label>
        <label>
          End date
          <input v-model="form.endDate" type="date" required />
        </label>
        <div>
          <button type="submit" :disabled="loading">
            Save booking
          </button>
        </div>
      </form>
      <p v-if="error" class="muted">{{ error }}</p>
    </section>

    <section class="card stack">
      <div>
        <h2>Bookings</h2>
        <p class="muted" v-if="loading">Loading...</p>
      </div>
      <div>
        <button type="button" :disabled="loading" @click="loadBookings()">
          Refresh
        </button>
      </div>
      <table v-if="bookings.length">
        <thead>
          <tr>
            <th>Guest</th>
            <th>Apartment</th>
            <th>Start</th>
            <th>End</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(booking, index) in bookings" :key="index">
            <td>{{ booking.guestName }}</td>
            <td>{{ booking.apartmentName }}</td>
            <td>{{ booking.startDate }}</td>
            <td>{{ booking.endDate }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else class="muted">No bookings yet.</p>
    </section>
  </div>
</template>
