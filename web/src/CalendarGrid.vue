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
              :class="['booking-segment', segment.apartmentClass]"
              :style="{
                gridColumn: `${segment.startIndex} / span ${segment.length}`,
                '--segment-offset-left': `${segment.offsetLeftPx ?? 0}px`,
                gridRow: `${(segment.laneIndex ?? 0) + 1}`,
              }"
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
</template>

<style scoped>
.calendar {
  display: grid;
  gap: 0;
  --cell-size: 30px;
  --header-height: 22px;
  --booking-height: 36px;
  --border-color: #cfcfcf;
  --skew: 20deg;
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
  overflow-x: auto;
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
  background: #078f70;
}

.weekday {
  background: #117b91;
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
  pointer-events: none;
  border: 1px;
  transform: translateX(1px);
  grid-auto-rows: var(--segment-row-height);
  row-gap: var(--segment-row-gap);
}

.booking-segment {
  height: var(--segment-row-height);
  display: flex;
  align-items: center;
  padding: 6px;
  z-index: 0;
  overflow: hidden;
  transform: translate(
      var(--segment-offset-left),
      0px
    )
    skewX(var(--skew));
  transform-origin: left top;
}

.booking-segment-garten {
  background: rgba(120, 200, 80, 0.85);
  border: 1px solid #4a9b2f;
}

.booking-segment-parkblick {
  background: rgba(246, 117, 26, 0.89);
  border: 1px solid #af7120;
}

.booking-segment-weitblick {
  background: rgba(67, 216, 183, 0.78);
  border: 1px solid #20FFFF77;
}

.booking-segment-text {
  font-size: 11px;
  color: #0f2b0f;
  padding: 50px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transform: skewX(calc(-1 * var(--skew)));
  transform-origin: left top;
}

.month-blue {
  background: #3a8797;
}

.month-green {
  background: #18987c;
}
</style>
