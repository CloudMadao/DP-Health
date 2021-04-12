<template>
  <div class="app-container">
    <div id="main" class="pie-class" :style="{width: '600px', height: '400px' } " ></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  mounted() {
    this.test();
  },

  methods:{
    test(){
      let chartDom = document.getElementById('main');
      let myChart = echarts.init(chartDom);
      let option;

      option = {
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [120, 200, 150, 80, 70, 110, 130],
          type: 'bar'
        }]
      };

      option && myChart.setOption(option);
    }
  },
}
</script>
