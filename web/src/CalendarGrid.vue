<script setup>
import { ref } from "vue";

const props = defineProps({
  months: {
    type: Array,
    required: true,
  },
  year: {
    type: Number,
    required: false,
  },
});

const hoveredSegment = ref(null);
const editSegment = ref(null);
const tooltipStyle = ref({ top: "0px", left: "0px" });

const editBooking = (segment) => {
  editSegment.value = segment
}

const showTooltip = (segment, event) => {
  hoveredSegment.value = segment;
  moveTooltip(event);
};

const moveTooltip = (event) => {
  const offset = 12;
  const maxWidth = 260;
  const maxHeight = 140;
  let left = event.clientX + offset;
  let top = event.clientY + offset;
  const vw = window.innerWidth;
  const vh = window.innerHeight;
  if (left + maxWidth > vw) {
    left = vw - maxWidth - offset;
  }
  if (top + maxHeight > vh) {
    top = vh - maxHeight - offset;
  }
  tooltipStyle.value = {
    left: `${Math.max(8, left)}px`,
    top: `${Math.max(8, top)}px`,
  };
};

const hideTooltip = () => {
  hoveredSegment.value = null;
};
</script>

<template>
  <div class="calendar">
    <div v-for="month in months" :key="month.name" class="month-row">
      <div class="month-label" :class="month.colorClass">
        <div class="month-name">{{ month.name }}</div>
        <div v-if="year" class="month-year">{{ year }}</div>
      </div>

      <div class="month-days">
        <div class="grid-row">
          <div
              v-for="day in month.days"
              :key="day.date + '-wd'"
              :class="['grid-cell', 'header-cell', month.colorClass]"
          > {{ day.weekdayLabel }}
          </div>
        </div>

        <div class="grid-row">
          <div
              v-for="day in month.days"
              :key="day.date + '-dn'"
              :class="['grid-cell', 'header-cell', month.colorClass]"
          >
            {{ day.dayNumber }}
          </div>
        </div>

        <div
          class="booking-row"
          :style="{
            '--booking-height': `${month.segmentStackHeight}px`,
            '--segment-row-height': `${month.segmentRowHeight}px`,
            '--segment-row-gap': `${month.segmentRowGap}px`,
          }"
        >
          <div class="booking-cells">
            <div class="grid-cell booking-cell spacer-booking-cell"></div>
            <div
              v-for="day in month.days"
              :key="day.date + '-bk'"
              class="grid-cell booking-cell"
              :class="day.isWeekend ? 'weekend' : 'weekday'"
            ></div>
          </div>
          <div class="booking-segments">
            <div
              v-for="(segment, index) in month.segments"
              :key="month.name + '-seg-' + index"
              :class="['booking-segment',
                segment.apartmentName === 'Garten' && 'booking-segment-garten',
                segment.apartmentName === 'Parkblick' && 'booking-segment-parkblick',
                segment.apartmentName === 'Weitblick' && 'booking-segment-weitblick',
              ]"
              :style="{
                gridColumn: `${segment.startIndex} / span ${segment.length}`,
                '--segment-offset-left': `${segment.offsetLeftPx ?? 0}px`,
                gridRow: `${(segment.laneIndex ?? 0) + 1}`,
              }"
              @click="editBooking(segment, $event)"
              @mouseenter="showTooltip(segment, $event)"
              @mousemove="moveTooltip($event)"
              @mouseleave="hideTooltip"
            >
              <span class="booking-segment-text">
                {{ segment.guestName }}
              </span>
            </div>
          </div>
        </div>

        <div class="grid-row">
          <div class="grid-cell header-cell spacer"></div>
          <div
              v-for="day in [...month.days.slice(1), month.days[0]]"
              :key="day.date + '-dn'"
              :class="['grid-cell', 'header-cell', month.colorClass]"
          >
            {{ day.dayNumber }}
          </div>
        </div>
      </div>
    </div>
  </div>

  <teleport to="body">
    <div
      v-if="hoveredSegment"
      class="booking-tooltip booking-tooltip-floating"
      role="tooltip"
      :style="tooltipStyle"
    >
      <div class="tooltip-title">{{ hoveredSegment.guestName }}</div>
      <div class="tooltip-row">
        <span class="tooltip-label">BookingID</span>
        <span class="tooltip-value">{{hoveredSegment.bookingId}}</span>
      </div>
      <div class="tooltip-row">
        <span class="tooltip-label">Apartment</span>
        <span class="tooltip-value">{{ hoveredSegment.apartmentName }}</span>
      </div>
      <div class="tooltip-row">
        <span class="tooltip-label">Zeitraum</span>
        <span class="tooltip-value">
          {{ hoveredSegment.bookingStart }} - {{ hoveredSegment.bookingEnd }}
        </span>
      </div>
      <div class="tooltip-row">
        <span class="tooltip-label">Übernachtungen</span>
        <span class="tooltip-value">{{ hoveredSegment.totalNights }}</span>
      </div>
    </div>
  </teleport>
</template>

<style scoped>
.calendar {
  display: grid;
  gap: 0;
  --cell-size: 30px;
  --header-height: 22px;
  --booking-height: 36px;
  --border-color: #cfcfcf;
  --skew: 21deg;
  --segment-offset-left: 0px;
  --segment-row-height: 24px;
  --segment-row-gap: 4px;
}

.spacer {
  background: transparent;
  border: 1px solid var(--border-color);
}



.month-row {
  display: flex;
}

.month-label {
  width: 100px;
  border: 1px solid var(--border-color);
  padding: 6px;
}

.month-name {
  font-weight: 600;
}

.month-year {
  font-size: 12px;
  color: #666;
}

.month-days {
  overflow-x: clip;
  display: inline-block;
}

.grid-row {
  display: grid;
  grid-auto-flow: column;
  grid-auto-columns: var(--cell-size);
  width: max-content;
}

.grid-cell {
  border: 1px solid var(--border-color);
  width: var(--cell-size);
  display: grid;
  place-items: center;
  font-size: 12px;
}

.header-cell {
  height: var(--header-height);
}

.booking-cell {
  height: var(--booking-height);
  position: relative;
  overflow: hidden;
  transform: skewX(var(--skew));
  transform-origin: left bottom;
}

.weekend {
  background: var(--calendar-theme-green);
}

.weekday {
  background: var(--calendar-theme-blue);
}

.booking-row {
  position: relative;
  height: var(--booking-height);
  width: max-content;
}

.booking-cells,
.booking-segments {
  display: grid;
  grid-auto-flow: column;
  grid-auto-columns: var(--cell-size);
  height: var(--booking-height);
  width: max-content;
}

.booking-segments {
  align-content: start;
  position: absolute;
  inset: 0;
  border: 1px;
  transform: translateX(-1px);
  grid-auto-rows: var(--segment-row-height);
  row-gap: var(--segment-row-gap);
}

.booking-segment {
  height: var(--segment-row-height);
  display: flex;
  align-items: center;
  padding: 6px;
  z-index: 10;
  overflow: visible;
  position: relative;
  transform: translate(
      var(--segment-offset-left),
      0px
    )
    skewX(var(--skew));
  transform-origin: left top;
}

.booking-segment-text {
  font-size: 11px;
  color: #0f2b0f;
  padding: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transform: skewX(calc(-1 * var(--skew)));
  transform-origin: left top;
}

.booking-tooltip {
  position: fixed;
  width: 220px;
  padding: 10px 12px;
  border-radius: 10px;
  background: #3a8797;
  color: #f5f5f5;
  box-shadow: 0 12px 24px rgb(2, 67, 87);
  opacity: 1;
  transform: translateY(0);
  pointer-events: none;
  z-index: 1000;
}

.tooltip-title {
  font-size: 12px;
  font-weight: 600;
  margin-bottom: 6px;
  z-index: 50;
}

.tooltip-row {
  display: flex;
  justify-content: space-between;
  font-size: 11px;
  line-height: 1.4;
  z-index: 50;
}

.tooltip-label {
  color: #aab3bd;
  margin-right: 8px;
}

.tooltip-value {
  color: #f5f5f5;
  text-align: right;
  z-index: 50;
}

</style>