<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-dropdown trigger="click" :hide-on-click="false">
          <el-button type="warning" size="mini" >
            信息统计<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>
              <el-form :model="queryParams.sguardiancountry"  :inline="true">

                <el-form-item label="患者地址" prop="sguardiancountry">
                  <el-select v-model="queryParams.sguardiancountry"  placeholder="请选择县区">
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>

                <el-form-item>
                  <el-button type="cyan" icon="el-icon-search" size="mini" @click="countryCount">统计</el-button>
                </el-form-item>
              </el-form>
            </el-dropdown-item>
            <!--            <el-dropdown-item>黄金糕</el-dropdown-item>
                        <el-dropdown-item>狮子头</el-dropdown-item>-->
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <div id="main" class="pie-class" :style="{width: '1600px', height: '800px' }"/>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import {changeRoleStatus, dataScope, getRole} from "@/api/system/role";
import {roleMenuTreeselect, treeselect as menuTreeselect} from "@/api/system/menu";
import {roleDeptTreeselect, treeselect as deptTreeselect} from "@/api/system/dept";
import {delJsb, jmJsb, listJsb} from "@/api/ttjm/ttjm";
import {groupCountry, groupCountryPost} from "@/api/dp/dp";
import * as echarts from 'echarts';

export default {
  name: "Jsb",
  data() {
    return {

      testimage: require('@/assets/image/image.jpg'),
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 角色表格数据
      jsbList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      ihitimage: false,
      // 是否显示弹出层（数据权限）
      openDataScope: false,
      menuExpand: false,
      menuNodeAll: false,
      deptExpand: true,
      deptNodeAll: false,
      // 日期范围
      dateRange: [],
      // 状态数据字典
      statusOptions: [],
      // 数据范围选项
      dataScopeOptions: [
        {
          value: "1",
          label: "全部数据权限"
        },
        {
          value: "2",
          label: "自定数据权限"
        },
        {
          value: "3",
          label: "本部门数据权限"
        },
        {
          value: "4",
          label: "本部门及以下数据权限"
        },
        {
          value: "5",
          label: "仅本人数据权限"
        }
      ],

      options: [{
        value: '花山区',
        label: '花山区'
      }, {
        value: '雨山区',
        label: '雨山区'
      }, {
        value: '博望区',
        label: '博望区'
      }, {
        value: '当涂县',
        label: '当涂县'
      }, {
        value: '含山县',
        label: '含山县'
      }, {
        value: '和县',
        label: '和县'
      }],

      echartparam:[],
      echartparamx:[],
      echartparamy:[],

      // 菜单列表
      menuOptions: [],
      // 部门列表
      deptOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        Name: undefined,
        sguardiancountry: []
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
        schargephysician: '',
        region: ''
      },


      defaultProps: {
        children: "children",
        label: "label"
      },


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
  created() {
    this.getList();
  },
  methods: {
    /** 查询菜单树结构 */
    getMenuTreeselect() {
      menuTreeselect().then(response => {
        this.menuOptions = response.data;
      });
    },
    /** 查询部门树结构 */
    getDeptTreeselect() {
      deptTreeselect().then(response => {
        this.deptOptions = response.data;
      });
    },

    // 所有菜单节点数据
    getMenuAllCheckedKeys() {
      // 目前被选中的菜单节点
      let checkedKeys = this.$refs.menu.getHalfCheckedKeys();
      // 半选中的菜单节点
      let halfCheckedKeys = this.$refs.menu.getCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },
    // 所有部门节点数据
    getDeptAllCheckedKeys() {
      // 目前被选中的部门节点
      let checkedKeys = this.$refs.dept.getHalfCheckedKeys();
      // 半选中的部门节点
      let halfCheckedKeys = this.$refs.dept.getCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },
    /** 根据角色ID查询菜单树结构 */
    getRoleMenuTreeselect(roleId) {
      return roleMenuTreeselect(roleId).then(response => {
        this.menuOptions = response.menus;
        return response;
      });
    },
    /** 根据角色ID查询部门树结构 */
    getRoleDeptTreeselect(roleId) {
      return roleDeptTreeselect(roleId).then(response => {
        this.deptOptions = response.depts;
        return response;
      });
    },
    // 角色状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$confirm('确认要"' + text + '""' + row.roleName + '"角色吗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return changeRoleStatus(row.roleId, row.status);
      }).then(() => {
        this.msgSuccess(text + "成功");
      }).catch(function() {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.ihitimage=false;
      this.reset();
    },
    // 取消按钮（数据权限）
    cancelDataScope() {
      this.openDataScope = false;
      this.reset();
    },
    // 表单重置
    reset() {
      if (this.$refs.menu != undefined) {
        this.$refs.menu.setCheckedKeys([]);
      }
      this.menuExpand = false,
        this.menuNodeAll = false,
        this.deptExpand = true,
        this.deptNodeAll = false,
        this.form = {
          name: undefined,
          idCardNo: undefined,
          bmi: undefined,
          bmi_Suggest: undefined,
          arterial: undefined,
          fastingGlucose: undefined,
          drugName1: undefined,
          doctor: undefined
        };
      this.resetForm("form");
    },

    /** 按照地区统计患病信息*/
    countryCount() {
      return groupCountryPost(this.queryParams).then(response=>{
        this.echartparam=response.data;
        for(let key in this.echartparam){
          this.echartparamx.push(this.echartparam[key].property);
          this.echartparamy.push(this.echartparam[key].count);
        }
        let chartDom = document.getElementById('main');
        let myChart = echarts.init(chartDom);
        let option = {
          xAxis: {
            type: 'category',
            data: this.echartparamx,
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            data: this.echartparamy,
            type: 'bar',
            itemStyle: {
              normal: {
                label: {
                  show: true, //开启显示
                  position: 'top', //在上方显示
                  textStyle: { //数值样式
                    color: 'black',
                    fontSize: 16
                  }
                },
                color: '#2f4554',
              }
            },
          }]
        }
        this.$nextTick(() => {
          myChart.setOption(option);
        });
      })
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.sno = selection.map(item => item.sno)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    // 树权限（展开/折叠）
    handleCheckedTreeExpand(value, type) {
      if (type == 'menu') {
        let treeList = this.menuOptions;
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
        }
      } else if (type == 'dept') {
        let treeList = this.deptOptions;
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value;
        }
      }
    },
    // 树权限（全选/全不选）
    handleCheckedTreeNodeAll(value, type) {
      if (type == 'menu') {
        this.$refs.menu.setCheckedNodes(value ? this.menuOptions: []);
      } else if (type == 'dept') {
        this.$refs.dept.setCheckedNodes(value ? this.deptOptions: []);
      }
    },
    // 树权限（父子联动）
    handleCheckedTreeConnect(value, type) {
      if (type == 'menu') {
        this.form.menuCheckStrictly = value ? true: false;
      } else if (type == 'dept') {
        this.form.deptCheckStrictly = value ? true: false;
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      //this.getMenuTreeselect();
      this.open = true;
      this.title = "添加患者信息";
    },
    /** 分配数据权限操作 */
    handleDataScope(row) {
      this.reset();
      const roleDeptTreeselect = this.getRoleDeptTreeselect(row.roleId);
      getRole(row.roleId).then(response => {
        this.form = response.data;
        this.openDataScope = true;
        this.$nextTick(() => {
          roleDeptTreeselect.then(res => {
            this.$refs.dept.setCheckedKeys(res.checkedKeys);
          });
        });
        this.title = "分配数据权限";
      });
    },

    submitForm: function() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          setTimeout(() =>{
            this.msgSuccess("添加患者"+this.form.sguardianname+"成功");
            this.open = false;
            this.getList();
          },1050);
        } else {
          this.msgSuccess("添加患者"+this.form.sguardianname+"成功");
        }
      });
    },


    /** 提交按钮（数据权限） */
    submitDataScope: function() {
      if (this.form.roleId != undefined) {
        this.form.deptIds = this.getDeptAllCheckedKeys();
        dataScope(this.form).then(response => {
          if (response.code === 200) {
            this.msgSuccess("修改成功");
            this.openDataScope = false;
            this.getList();
          }
        });
      }
    },


    handleDelete() {
      const sno=this.sno;
      this.$confirm('是否确认删除身份证号为"' + sno + '"的数据项?', "警告", {
        distinguishCancelAndClose: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: "warning"
      })
        .then(() => {
          return delJsb(sno);
        })
        .then(()=>{
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(()=>{

        });
    },
    /** 解密按钮操作 */
    handleUpdate() {
      const sno=this.sno;
      jmJsb(sno).then(
        response => {
          //debugger
          this.jsbList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },

    showimage(){

    },

    ihitshow(){
      this.reset();
      //this.getMenuTreeselect();
      this.ihitimage = true;
      this.loading = false;
    },

    /** 查询角色列表 */
    getList() {
      this.loading = true;
      listJsb(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          //debugger
          this.jsbList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
  }
};
</script>



