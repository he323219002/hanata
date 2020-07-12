<template>
  <div class="category">
    <template>
      <el-button type="primary" style="float: left;margin: 2px 2px" @click="register">新增</el-button>
      <el-input class="search" v-model="search" placeholder="请输入内容"></el-input>
      <el-table
              :data=user.list
              style="width: 100%"
      >
        <el-table-column
                label="用户名"
                prop="username"
                fixed="left"
                width="150px"
        >
        </el-table-column>

        <el-table-column
                label="昵称"
                prop="nickname"
                width="150px"
        >
        </el-table-column>


        <el-table-column
                label="联系方式"
                prop="phone"
                width="150px"
        >
        </el-table-column>

        <el-table-column
                label="邮箱"
                prop="mail"
                width="150px"
        >
        </el-table-column>

        <el-table-column label="性别">
          <template slot-scope="scope">
            <p v-if="scope.row.gender==='1'">男</p>
            <p v-else>女</p>
          </template>
        </el-table-column>

        <el-table-column
                label="创建日期"
                prop="createTime"
                width="150px"
        >
        </el-table-column>

        <el-table-column
                label="最后登录"
                prop="lastLoginDate"
                width="150px"
        >
        </el-table-column>

        <el-table-column
                label="是否启用"
                fixed="right"
        >

          <template slot-scope="scope">
              <el-switch
                      v-model="scope.row.isActive"
                      active-color="#13ce66"
                      inactive-color="#ff4949"
                      active-value="1"
                      inactive-value="0"
                      @change="activeUser(scope.row.uid,scope.row.isActive)"
              >
              </el-switch>
          </template>
        </el-table-column>


      </el-table>

      <div class="block">
        <span class="demonstration"></span>
        <el-pagination
                layout="prev, pager, next"
                :total=user.total
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
    name: "User",
    created(){
      this.getUser();
    },
    data(){
      return {
        user:[],
        search:'',
      }
    },
    methods:{
      async getUser(){
        let {data:res} = await postRequest("user");
        checkToken(res);
        this.user=res.data
      },

      register(){
        this.$router.push({name:"NewUser"})
      },

      async activeUser(uid,active) {
        console.log(uid);
        let params = {
          "userId":uid,
          "active":active,
        }
        let {data:res} = await postRequest('user/active',params);
        console.log(res);
      },

      async handleCurrentChange(val){
        let Pageparams = {
          "limit": val+'',
          'q':this.search,
        }
        let {data:res} = await postRequest("user",Pageparams);
        if (res.code === 0) {
          console.log(res.data);
          this.user = res.data;
          // return []
        }
      }
    },
    watch:{
      search: async function(newVal,oldVal){
        let pageParams = {
          'q':this.search,
        }
        let {data:res} = await postRequest("user",pageParams);
        if (res.code === 0) {
          console.log(res.data);
          this.user = res.data;
        }
      }
    },
  }
</script>

<style lang="less" scoped>
  .search{
    width: 200px;
    float: left;
    margin: 2px;
  }
</style>