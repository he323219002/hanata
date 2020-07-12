<template>
  <div class="message" >
    <div class="per_message" v-for="item in message.list">
      <div class="title">
        {{item.title}}
      </div>

      <el-row>
        <el-col :span="2">
          <div class="userinfo">
            <div class="username">
              用户:{{item.userName}}
            </div>
            <div class="avatar">
              <img style="width: 100%;height: 100%" :src="picBaseURL + 'item.avatar'" v-if="item.avatar">
              <img style="width: 100%;height: 100%" v-else src="../assets/default-avatar-png.png">
            </div>
          </div>
        </el-col>

        <el-col :span="22">
          <div class="content">
            {{item.content}}
          </div>
        </el-col>
      </el-row>

      <div class="datetime">
        {{item.createTime}}
      </div>

    </div>

    <div class="pagination" style="transform: scale(1.2)">
      <el-pagination
              style="height: 100%"
              background
              :page-size="10"
              :pager-count="11"
              layout="prev, pager, next"
              :total="message.total"
              @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import {postRequest} from "../utils/api";
  import {picBaseURL} from "../utils/api";

  export default {
    name: "Message",
    created() {
      this.getMessage()
    },
    methods:{
      async getMessage(){
        let {data:res} = await postRequest('message');
        this.message=res.data
      },
      async handleCurrentChange(val){
        let Pageparams = {
          "limit": val+'',
        }
        let {data:res} = await postRequest("message",Pageparams);
        this.message = res.data;
      },
    },
    data(){
      return{
        message:'',
        picBaseURL:picBaseURL
      }
    }

  }
</script>

<style scoped>
  .message{
    padding: 10px;
    /*border: 1px solid #eeeeee;*/
  }
  .per_message{
    border: 1px solid #eeeeee;
    padding: 10px;
    height: 200px;
  }
  .avatar{
    height: 80px;
    width: 80px;
  }
  .content{
    font-size: 18px;
    transform: translate(0,100%);
  }
  .datetime{
    float: right;
    transform: translate(0,200%);
    font-size: small;
    color: gray;
  }
  .username{
    font-size: 20px;
  }
  .pagination{
    height: 60px;
    width: 100%;
    text-align: center;
    margin-top: 30px;
  }

</style>