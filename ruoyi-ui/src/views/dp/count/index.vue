<template>
  <div class="app-container">
  <el-row :gutter="10" class="mb8">

        <el-col :span="1.5">
          <!--  <el-button
              type="warning"
              size="mini"
              @click="showimage"
            >统计</el-button>-->
          <el-select v-model="DataBase" placeholder="请选择" @change="changeSelect">
            <el-option
              v-for="(item,index) in DataBaseOptions"
              :key="index"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-col>
        <el-col :span="1.5">
          <el-select v-model="DataBaseBrandSecond" placeholder="请选择" >
            <el-option
              v-for="(item,index) in DropDownTitle"
              :key="index"
              :label="item"
              :value="item"
            />
          </el-select>

        </el-col>

          <el-button type="cyan" icon="el-icon-search" size="mini" @click="ihitshow">数据统计</el-button>

        <!--按条件搜索-->
      </el-row>
      <!-- 分配角色数据权限对话框 -->

    <div id="main" class="pie-class" :style="{width: '1000px', height: '1000px' } "></div>
  </div>
</template>
//准备一个呈现图表的盒子
<script>
import * as echarts from 'echarts';//引入echarts.js文件
export default {
      data() {
      return {
       DataBaseOptions: ['精神病患者地区分布', '精神病患者肇事次数', '精神病患者轻度症状','精神病患者重度症状'],
       DropDownOptions: { '精神病患者地区分布': ['肇事次数', '自伤次数', '自杀次数','住院次数'],
                          '精神病患者肇事次数': ['感冒次数', '扭伤次数', '摔伤次数','健康次数'],
                          '精神病患者轻度症状': ['昏倒次数', '头晕次数', '其他次数','另外次数'],
                          '精神病患者重度症状': ['昏倒次数', '头晕次数', '其他次数','另外次数'],
                        },

        //testimage: require('@/assets/image/image.jpg'),
        // 遮罩层


        // 非多个禁用

        showSearch: false,
        // 总条数


        // 状态数据字典
        statusOptions: [],
        // 数据范围选项
        DataBase:"精神病数据统计",
        DataBaseBrandSecond:"肇事次数",
        DropDownTitle: ['肇事次数', '自伤次数', '自杀次数','住院次数'],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 20,
          Name: undefined,
        },
        // 表单参数
        form: {
          sguardianname: '',
          sno: '',
          spatienttelephone: '',
          sguardiantelephone: '',
          ssymptomscode: '',
          spsychosiscode: '',
          ihit: '',
          schargephysician: ''
        },
        //图表参数
        echartstitle: "",
        echartparam:[],
        echartparamx:[],
        echartparamy:[],

        defaultProps: {
          children: "children",
          label: "label"
        },
        //
        arraytj:[],


        // 表单校验
        rules: {
          sguardianname: [
            { required: true, message: "患者姓名不能为空", trigger: "blur" }
          ],
          sno: [
            { required: true, message: "患者不能为空", trigger: "blur" }
          ],
        }
      };
    },
  mounted() {
    this.test();

  },

  methods:{
    test(){
      let chartDom = document.getElementById('main');
      let myChart= echarts.init(chartDom);              //初始化echarts实例对象。参数，dom， 决定图表最终呈现的位置
      let option;
      var yDataArr = [280,190,320,180,200,240];
      var zDataArr = [3,2,5,6,1,2,3,4,2,5];
      option = {
            title: {
                text: '医疗健康管理系统相关图表',
                left: 'center',
                top: 0,

            },
            legend: {                   // legend 放在底部中间。
                            right: 'center',
                            bottom: 0,
                            orient: 'horizontal'    // legend 横向布局。
                        },
            grid: [
                        {
                        x:0,
                        y:100,
                        x2:500,
                        y2:500,
                        containLabel: true
                         },
                         {
                         x:550,
                         y:100,
                         x2:0,
                         y2:500,
                         containLabel: true
                         },
                         {
                         x:0,
                         y:550,
                         x2:500,
                         y2:100,
                         containLabel: true
                         }
                         ],
            xAxis: [
            {
              gridIndex: 0,
              type: 'value'//数值轴
            },//x轴
            {
              gridIndex: 1,
              type: 'category',//类目轴
              data: ['2011', '2012', '2013', '2014', '2015', '2016','2017', '2018', '2019', '2020']
            },
            {
              gridIndex: 2,
              scale: true,//类目轴
            }],//x轴
            yAxis:[
            {
              gridIndex: 0,
              type: 'category',//类目轴
              data: ['雨山区', '花山区', '博望区', '当涂县', '和县', '含山县']
            },
            {
              gridIndex: 1,
              type: 'value'//数值轴
            },
            {
            gridIndex: 2,
            scale: true,//类目轴
            }],
            toolbox:{
            feature:{
            saveAsImage:{},
            dataView:{},
            restore:{},
            dataZoom:{},
            magicType:{
            type:['bar','line'],
            },
            }
            },

            series: [
            {
                name: '精神病人数',
                xAxisIndex: 0,
                yAxisIndex: 0,
                data: yDataArr,
                type: 'bar',//柱状图
               markPoint: {
                          data:[
                          {type:'max',
                          name:'最大值'},
                           {type:'min',
                          name:'最小值'}]
                          },
               markLine: {
                         data:[
                         {type:'average',
                         name:'平均值'}]
                         },

                          },
                          //配置参数项
            {
                       name: '重症精神病名称代码',
                               type: 'pie',
                               radius: '25%',
                               center: ['80%','75%'],
                               roseType: 'angle',
                               data:[
                                   {value:130, name:'1精神分裂症'},
                                   {value:270, name:'2分裂情感性障碍'},
                                   {value:310, name:'3偏执性精神病'},
                                   {value:225, name:'4双相障碍'},
                                   {value:135, name:'5癫痫所致精神障碍'},
                                   {value:370, name:'6精神发育迟滞伴发精神障碍'},
                                   {value:510, name:'9其他'},
                               ]
            },
            {
                  name: '肇事次数',
                  xAxisIndex: 1,
                  yAxisIndex: 1,
                  data: zDataArr,
                  type: 'line',
                  smooth: 'yes'
            },
            {        xAxisIndex: 2,
                     yAxisIndex: 2,
                    type: 'effectScatter',
                    symbolSize: 20,
                    data: [
                        [172.7, 105.2],
                        [153.4, 42]
                    ]
                },
                {   name: '轻症精神病名称代码',
                    xAxisIndex: 2,
                    yAxisIndex: 2,
                    type: 'scatter',
                    data: [[161.2, 51.6], [167.5, 59.0], [159.5, 49.2], [157.0, 63.0], [155.8, 53.6],
                        [170.0, 59.0], [159.1, 47.6], [166.0, 69.8], [176.2, 66.8], [160.2, 75.2],
                        [172.5, 55.2], [170.9, 54.2], [172.9, 62.5], [153.4, 42.0], [160.0, 50.0],
                        [147.2, 49.8], [168.2, 49.2], [175.0, 73.2], [157.0, 47.8], [167.6, 68.8],
                        [159.5, 50.6], [175.0, 82.5], [166.8, 57.2], [176.5, 87.8], [170.2, 72.8],
                        [174.0, 54.5], [173.0, 59.8], [179.9, 67.3], [170.5, 67.8], [160.0, 47.0],
                        [154.4, 46.2], [162.0, 55.0], [176.5, 83.0], [160.0, 54.4], [152.0, 45.8],
                        [162.1, 53.6], [170.0, 73.2], [160.2, 52.1], [161.3, 67.9], [166.4, 56.6],
                        [168.9, 62.3], [163.8, 58.5], [167.6, 54.5], [160.0, 50.2], [161.3, 60.3],
                        [167.6, 58.3], [165.1, 56.2], [160.0, 50.2], [170.0, 72.9], [157.5, 59.8],
                        [167.6, 61.0], [160.7, 69.1], [163.2, 55.9], [152.4, 46.5], [157.5, 54.3],
                        [168.3, 54.8], [180.3, 60.7], [165.5, 60.0], [165.0, 62.0], [164.5, 60.3],
                        [156.0, 52.7], [160.0, 74.3], [163.0, 62.0], [165.7, 73.1], [161.0, 80.0],
                        [162.0, 54.7], [166.0, 53.2], [174.0, 75.7], [172.7, 61.1], [167.6, 55.7],
                        [151.1, 48.7], [164.5, 52.3], [163.5, 50.0], [152.0, 59.3], [169.0, 62.5],
                        [164.0, 55.7], [161.2, 54.8], [155.0, 45.9], [170.0, 70.6], [176.2, 67.2],
                        [170.0, 69.4], [162.5, 58.2], [170.3, 64.8], [164.1, 71.6], [169.5, 52.8],
                        [163.2, 59.8], [154.5, 49.0], [159.8, 50.0], [173.2, 69.2], [170.0, 55.9],
                        [161.4, 63.4], [169.0, 58.2], [166.2, 58.6], [159.4, 45.7], [162.5, 52.2],
                        [159.0, 48.6], [162.8, 57.8], [159.0, 55.6], [179.8, 66.8], [162.9, 59.4],
                        [161.0, 53.6], [151.1, 73.2], [168.2, 53.4], [168.9, 69.0], [173.2, 58.4],
                        [171.8, 56.2], [178.0, 70.6], [164.3, 59.8], [163.0, 72.0], [168.5, 65.2],
                        [166.8, 56.6], [172.7, 105.2], [163.5, 51.8], [169.4, 63.4], [167.8, 59.0],
                        [159.5, 47.6], [167.6, 63.0], [161.2, 55.2], [160.0, 45.0], [163.2, 54.0],
                        [162.2, 50.2], [161.3, 60.2], [149.5, 44.8], [157.5, 58.8], [163.2, 56.4],
                        [172.7, 62.0], [155.0, 49.2], [156.5, 67.2], [164.0, 53.8], [160.9, 54.4],
                        [162.8, 58.0], [167.0, 59.8], [160.0, 54.8], [160.0, 43.2], [168.9, 60.5],
                        [158.2, 46.4], [156.0, 64.4], [160.0, 48.8], [167.1, 62.2], [158.0, 55.5],
                        [167.6, 57.8], [156.0, 54.6], [162.1, 59.2], [173.4, 52.7], [159.8, 53.2],
                        [170.5, 64.5], [159.2, 51.8], [157.5, 56.0], [161.3, 63.6], [162.6, 63.2],
                        [160.0, 59.5], [168.9, 56.8], [165.1, 64.1], [162.6, 50.0], [165.1, 72.3],
                        [166.4, 55.0], [160.0, 55.9], [152.4, 60.4], [170.2, 69.1], [162.6, 84.5],
                        [170.2, 55.9], [158.8, 55.5], [172.7, 69.5], [167.6, 76.4], [162.6, 61.4],
                        [167.6, 65.9], [156.2, 58.6], [175.2, 66.8], [172.1, 56.6], [162.6, 58.6],
                        [160.0, 55.9], [165.1, 59.1], [182.9, 81.8], [166.4, 70.7], [165.1, 56.8],
                        [177.8, 60.0], [165.1, 58.2], [175.3, 72.7], [154.9, 54.1], [158.8, 49.1],
                        [172.7, 75.9], [168.9, 55.0], [161.3, 57.3], [167.6, 55.0], [165.1, 65.5],
                        [175.3, 65.5], [157.5, 48.6], [163.8, 58.6], [167.6, 63.6], [165.1, 55.2],
                        [165.1, 62.7], [168.9, 56.6], [162.6, 53.9], [164.5, 63.2], [176.5, 73.6],
                        [168.9, 62.0], [175.3, 63.6], [159.4, 53.2], [160.0, 53.4], [170.2, 55.0],
                        [162.6, 70.5], [167.6, 54.5], [162.6, 54.5], [160.7, 55.9], [160.0, 59.0],
                        [157.5, 63.6], [162.6, 54.5], [152.4, 47.3], [170.2, 67.7], [165.1, 80.9],
                        [172.7, 70.5], [165.1, 60.9], [170.2, 63.6], [170.2, 54.5], [170.2, 59.1],
                        [161.3, 70.5], [167.6, 52.7], [167.6, 62.7], [165.1, 86.3], [162.6, 66.4],
                        [152.4, 67.3], [168.9, 63.0], [170.2, 73.6], [175.2, 62.3], [175.2, 57.7],
                        [160.0, 55.4], [165.1, 104.1], [174.0, 55.5], [170.2, 77.3], [160.0, 80.5],
                        [167.6, 64.5], [167.6, 72.3], [167.6, 61.4], [154.9, 58.2], [162.6, 81.8],
                        [175.3, 63.6], [171.4, 53.4], [157.5, 54.5], [165.1, 53.6], [160.0, 60.0],
                        [174.0, 73.6], [162.6, 61.4], [174.0, 55.5], [162.6, 63.6], [161.3, 60.9],
                        [156.2, 60.0], [149.9, 46.8], [169.5, 57.3], [160.0, 64.1], [175.3, 63.6],
                        [169.5, 67.3], [160.0, 75.5], [172.7, 68.2], [162.6, 61.4], [157.5, 76.8],
                        [176.5, 71.8], [164.4, 55.5], [160.7, 48.6], [174.0, 66.4], [163.8, 67.3]
                    ],
                }

    ]

      };
      option && myChart.setOption(option);

      // 调用 setOption 将 option 输入 echarts，然后 echarts 渲染图表。
    }
  },
}
</script>
