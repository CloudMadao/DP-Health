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
              :key="index3"
              :label="item"
              :value="item"
            />
          </el-select>
          <el-button type="cyan" icon="el-icon-s-data"  @click="countryCount">数据统计</el-button>
        </el-col>
<!--        <el-col :span="1.5">
          <el-select v-model="DataBaseBrandSecond" placeholder="请选择" >
            <el-option
              v-for="(item,index) in DropDownTitle"
              :key="index"
              :label="item"
              :value="item"
            />
          </el-select>

        </el-col>-->

          <el-button type="cyan" icon="el-icon-s-data"  @click="countryCount">数据统计</el-button>


        <!--按条件搜索-->
      </el-row>
      <!-- 分配角色数据权限对话框 -->

    <div id="main" class="pie-class" :style="{width: '1000px', height: '1000px' } "></div>
  </div>
</template>
//准备一个呈现图表的盒子
<script>
import * as echarts from 'echarts';
//引入echarts.js文件
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
                         }],
            xAxis: [
            {
              gridIndex: 0,
              type: 'category',//类目轴
              data: ['雨山区', '花山区', '博望区', '当涂县', '和县', '含山县']
            },//x轴
            {
              gridIndex: 1,
              type: 'category',//类目轴
              data: ['2011', '2012', '2013', '2014', '2015', '2016','2017', '2018', '2019', '2020']
            }],//x轴
            yAxis:[
            {
              gridIndex: 0,
              type: 'value'//数值轴
            },
            {
              gridIndex: 1,
              type: 'value'//数值轴
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
                               center: ['50%','80%'],
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
    ]

      };
      option && myChart.setOption(option);

      // 调用 setOption 将 option 输入 echarts，然后 echarts 渲染图表。
    }
  },

}
</script>
