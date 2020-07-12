<template>
  <div class="record">
    <div class="data">
      <el-table
              :data="tableData"
              border
              height="600"
              style="width: 100%">
        <el-table-column
                label="序号"
                width="180">
          <template slot-scope="scope">
            <p >{{scope.$index + 1}}</p>
          </template>
        </el-table-column>
        <el-table-column
                prop="ip"
                label="ip"
                width="180">
        </el-table-column>
        <el-table-column
                prop="url"
                label="url">
        </el-table-column>
        <el-table-column
                prop="createTime"
                label="日期">
        </el-table-column>
      </el-table>
    </div>

    <div class="block">
      <span class="demonstration"></span>
      <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-sizes="[100, 200, 300, 400]"
              :page-size="100"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
      </el-pagination>
    </div>

  </div>

</template>

<script>
  import {postRequest} from "../utils/api";

  export default {
    name:"Record",
    created(){
      this.getRecordData()
    },
    methods: {
      handleSizeChange(val) {
        this.recordForm.size=val+''
        this.getRecordData()
      },
      handleCurrentChange(val) {
        this.recordForm.page=val+''
        this.getRecordData()
      },
      async getRecordData(){
        let {data:res} = await postRequest('visitor',this.recordForm);
        console.log(res);
        this.tableData=res.data.data
        this.currentPage=res.data.currentPage
        this.total=res.data.total
      }
    },
    data() {
      return {
        total:0,
        currentPage: 1,
        recordForm:{
          page:'1',
          size:'100',
        },
        tableData: []
      }
    },

  }
</script>

<style scoped>

</style>