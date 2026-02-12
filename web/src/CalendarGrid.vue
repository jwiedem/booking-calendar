<script setup>
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
</script>

<template>
  <div class="calendar">
    <div v-for="month in months" :key="month.name" class="month-row">
      <div class="month-label" :class="month.colorClass">
        <div class="month-name">{{ month.name }}</div>
        <div v-if="year" class="month-year">{{ year }}</div>
      </div>

      <div class="month-days">
        <div class="weekday-row">
          <div
              v-for="day in month.days"
              :key="day.date + '-wd'"
              :class="['header-cell', month.colorClass]"
          > {{ day.weekdayLabel }}
          </div>
        </div>

        <div class="daynumber-row">
          <div
              v-for="day in month.days"
              :key="day.date + '-dn'"
              :class="['header-cell', month.colorClass]"
          >
            {{ day.dayNumber }}
          </div>
        </div>

        <div class="booking-row">
          <div class="booking-cells">
            <div class="booking-cell spacer-booking-cell"></div>
            <div
              v-for="day in month.days"
              :key="day.date + '-bk'"
              class="booking-cell"
              :class="day.isWeekend ? 'weekend' : 'weekday'"
            ></div>
          </div>
          <div class="booking-segments">
            <div class="booking-segment-spacer"></div>
            <div
              v-for="(segment, index) in month.segments"
              :key="month.name + '-seg-' + index"
              class="booking-segment-garten"
              :style="{ gridColumn: `${segment.startIndex} / span ${segment.length}` }"
            >
              <span class="booking-segment-text">
                {{ segment.guestName }}
              </span>
            </div>
          </div>
        </div>

        <div class="daynumber-row">
          <div class="header-cell spacer"></div>
          <div
              v-for="day in [...month.days.slice(1), month.days[0]]"
              :key="day.date + '-dn'"
              :class="['header-cell', month.colorClass]"
          >
            {{ day.dayNumber }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.calendar {
  display: grid;
  gap: 0;
}

.spacer {
  background: transparent;
  border: 1px solid #cfcfcf; /* or none */
}

.spacer-booking-cell {
  height: 36px;
  position: relative;
  overflow: hidden;
  transform: skewX(38deg);
  transform-origin: left bottom;
}

.month-row {
  display: flex;
}

.month-label {
  width: 100px;
  border: 1px solid #cfcfcf;
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
  overflow-x: auto;
  display: inline-block;
}

.weekday-row{
  display: grid;
  grid-auto-flow: column;
  grid-auto-columns: 28px;
  width: max-content;
}
.daynumber-row {
  display: grid;
  grid-auto-flow: column;
  grid-auto-columns: 28px;
  width: max-content;
}

.header-cell,
.booking-cell {
  border: 1px solid #cfcfcf;
  width: 28px;
  height: 22px;
  display: grid;
  place-items: center;
  font-size: 12px;
}

.booking-cell {
  height: 36px;
  position: relative;
  overflow: hidden;
  transform: skewX(38deg);
  transform-origin: left bottom;
}

.weekend {
  background: #078f70;
}

.weekday {
  background: #117b91;;
}

.booking-row {
  position: relative;
  height: 36px;
  width: max-content;
}

.booking-cells,
.booking-segments {
  display: grid;
  grid-auto-flow: column;
  grid-auto-columns: 28px;
  height: 36px;
  width: max-content;
}

.booking-segments {
  position: absolute;
  inset: 0;
  pointer-events: none;
  border: 1px;
}

.booking-segment-spacer {
  height: 36px;
}

.booking-segment-garten {
  background: #78c850;
  border: 1px solid #4a9b2f;
  height: 12px;
  display: flex;
  align-items: center;
  padding: 6px;
  z-index: 0;
  overflow: hidden;
  transform: skewX(38deg);
  transform-origin: left top;
}

.booking-segment-parkblick {
  background: #78c850;
  border: 1px solid #4a9b2f;
  height: 12px;
  display: flex;
  align-items: center;
  padding: 6px;
  z-index: 0;
  overflow: hidden;
  transform: skewX(38deg);
  transform: translateY(6px);
  transform-origin: left top;
}

.booking-segment-text {
  font-size: 11px;
  color: #0f2b0f;
  padding: 50px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transform: skewX(-38deg);
  transform-origin: left top;
}

.month-blue {
  background: #3a8797;
}

.month-green {
  background: #18987c;
}
</style>
