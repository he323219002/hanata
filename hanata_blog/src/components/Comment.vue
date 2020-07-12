<template>
  <div class="comment" >

    <div class="per-comment" v-for="item in comment.list">
      <p v-if="item.fartherContent">回复：{{item.fartherContent}}</p>
      <div class="user-info">
        <div class="username">
          用户：{{item.userName}}
        </div>
        <div class="user-img">
          <img :src="'picBaseURL' + item.userAvatar">
        </div>

      </div>

      <div class="detail">
        {{item.content}}
        <a style="cursor: pointer" @click="reply(item.userName,item.uid)">回复</a>
      </div>


      <div class="date">
        posted: {{item.createTime}}
      </div>
    </div>

    <div class="pagination" style="transform: scale(1.2)">
      <el-pagination
              style="height: 100%;text-align: center;margin: 20px"
              background
              :page-size="10"
              :pager-count="11"
              layout="prev, pager, next"
              :total="comment.total"
              @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>


  </div>
</template>

<script>
  import {postRequest} from "../utils/api";
  import {checkToken} from "../utils/utils";
  import {picBaseURL} from "../utils/api";

  export default {
    name: "Comment",
    methods:{
      async getCommentById(id,kind){
        let {data:res} = await postRequest('comment',{'articleId':id,'kind':kind});
        checkToken(res)
        this.comment=res.data
        console.log(this.comment);
      },
      async handleCurrentChange(val){
        let Pageparams = {
          "articleId":this.$route.query.id,
          "limit": val+'',
        }
        let {data:res} = await postRequest("comment",Pageparams);
        this.comment = res.data;
        console.log(val);
      },
      reply(username,fid){
        console.log(username);

        this.$emit('commenter',username,fid)
      }
    },
    data(){
      return{
        comment:'',
        picBaseURL:picBaseURL,
        total:''
      }
    },
    props:['id']
  }
</script>

<style lang="less" scoped>
  .comment{
    transform: translate(-1%);
  }
  .per-comment{
    a{
      text-decoration: none;
      color: lightskyblue;
      float: right;
    }
    margin: 10px;
    width: 100%;
    height: 100px;
    border: 1px dashed;

    /*border: 1px dashed #eeeeee;*/
  }
  .username{
    font-size: 15px;
  }
  .user-info{
    border: 1px solid greenyellow;
    float:left;
    width: 20%;
    height: 100%;
  }
  .date{
    font-size: 14px;
    color: gray;
    float: right;
  }
  .detail{
    margin-left: 20%;
    width: 80%;
    border: 1px solid;
  }
  .date{
    transform: translate(0,300%);
  }
</style>