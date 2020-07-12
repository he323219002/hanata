<template>
  <div class="update">
    <template>
      <el-button type="primary" style="float: left;margin: 2px 2px" @click="handleNew">新增</el-button>

      <el-table
              :data=update.list
              style="width: 100%"
      >
        <el-table-column
                label="版本"
                prop="version"
        >
        </el-table-column>

        <el-table-column
                label="内容"
                prop="content">
        </el-table-column>

        <el-table-column
                label="日期"
                prop="createTime">
        </el-table-column>

        <el-table-column
                align="right">




          <template slot-scope="scope">
            <el-button
                    size="mini"
                    @click="handleEdit(scope.$index, scope.row)"
                    @click.stop.native>编辑
            </el-button>

            <el-popconfirm
                    title="确定删除吗？"
                    @onConfirm="handleDelete(scope.$index, scope.row)"
                    @click.stop.native
            >
              <el-button
                      slot="reference"
                      size="mini"
                      type="danger">删除
              </el-button>
            </el-popconfirm>
          </template>

        </el-table-column>

      </el-table>

      <div class="block">
        <span class="demonstration"></span>
        <el-pagination
                layout="prev, pager, next"
                :total=update.total
                @current-change="handleCurrentChange"
        >
        </el-pagination>
      </div>


    </template>
  </div>
</template>

<script>
  import {postRequest} from "../../utils/api";
  import {checkToken} from "../../utils/utils";

  export default {
    name: "Update",
    created(){
      this.getUpdate();
    },
    data(){
      return {
        update:[],
      }
    },
    methods:{
      async getUpdate(){
        let {data:res} = await postRequest("update");
        console.log(res);
        checkToken(res);
        this.update = res.data
      },


      handleNew(){
        this.$router.push({name:"NewUpdate"})
      },
      handleEdit(index, row) {
        let updateId = row.uid
        let version = row.version
        let content = row.content
        let payload = {
          'id':updateId,
          'version':version,
          'content':content
        }
        this.$store.commit('setUpdateData',payload)

        this.$router.push({name:'UpdateUpdate'})
      },


      async handleDelete(index, row) {
        let {data:res} = await postRequest('update/del',{'id':row.uid});
        if (res.code===0){
          this.$router.go(0)
        } else {
          console.log(res.data());
        }
      },

      async handleCurrentChange(val){
        let Pageparams = {
          "limit": val+'',
        }
        let {data:res} = await postRequest("update",Pageparams);
        if (res.code === 0) {
          console.log(res.data);
          this.update = res.data;
          // return []

        }
      }
    },
    watch:{
      search: async function(newVal,oldVal){
        console.log(newVal);
        console.log(oldVal);
        let pageParams = {
          'q':this.search,
        }
        let {data:res} = await postRequest("category",pageParams);
        if (res.code === 0) {
          console.log(res.data);
          this.category = res.data;
        }
      }
    },
  }
</script>

<style scoped>

</style>