<template>
  <div class="article_detail">
    <div class="title">
      {{article.title}}
    </div>



    <div class="content">
      <vue-markdown :source="article.content"></vue-markdown>
    </div>

    <div class="article_comment article_footer">
      评论数: {{article.commentCount}}
    </div>

    <div class="article_time article_footer">
      post time：{{transtime(article.createTime)}}
    </div>

    <div class="like">
      <vue-star animate="animated rubberBand" color="#F05654">
        <a slot="icon" class="el-icon-thumb" @click="handleClick">点赞({{likeCount}})</a>
      </vue-star>
    </div>




    <el-row>
      <el-col :span="3">
        <div class="skip_article pre_article" >
          <p v-if="article.preName" @click="skipArticle(article.preId)">上一篇:{{article.preName}}</p>
          <p v-else>上一篇:没有啦</p>
        </div>
      </el-col>

      <el-col :span="3" :offset="16">
        <div class="skip_article next_article" >
         <p v-if="article.nextName" @click="skipArticle(article.nextId)">下一篇：{{article.nextName}}</p>
         <p v-else>下一篇：没有啦</p>
        </div>
      </el-col>
    </el-row>

    <div class="comment">
      <article-comment @commenter="receiveId" ref="child" ></article-comment>
      <new-comment :id="id" :kind="'1'" ref="new_comment"></new-comment>
    </div>

  </div>
</template>

<script>
  import {postRequest} from "../utils/api";
  import {checkToken} from "../utils/utils";
  import articleComment from "../components/Comment"
  import newComment from "../components/NewComment"


  export default {
    name: "ArticleDetail",
    data(){
      return{
        article:'',
        likeCount:'',
        id:'',
        like:false
      }
    },
    created(){
      this.getArticleById();
    },
    mounted(){
      this.$refs.child.getCommentById(this.id,'1')
    },
    methods:{
      async getArticleById(){
        let id = this.$route.query.id;
        this.id=id
        let {data:res} = await postRequest('article/show',{'uid':id});
        checkToken(res)
        this.article = res.data
        this.likeCount = this.article.likeCount

      },
      transtime(time = +new Date()){
        var date = new Date(time + 8 * 3600 * 1000);
        return date.toJSON().substr(0, 19).replace('T', ' ').replace(/-/g, '.');
      },
      skipArticle(id){
        this.$router.push({path:'/article/detail',query:{'id':id}})
      },
      receiveId(username,fid,targetId){
        console.log(username);
        this.$refs.new_comment.focus(username,fid,targetId)
      },
      async handleClick(){

        if (this.like){
          let {data:res} = await postRequest('like/cancel',{'articleId':this.id});
          checkToken(res)
          if (res.code===0){
            this.like=false
            this.likeCount -= 1
          }


        } else{
          let {data:res} = await postRequest('like/new',{'articleId':this.id})
          checkToken(res)
          if (res.code===0){
            this.like=true
            this.likeCount += 1
          }
        }
      }
    },
    watch:{
      '$route'(to,from){
        if (to.query.id !== from.query.id){
          this.getArticleById();
        }
      }
    },
    components:{articleComment,newComment}

  }
</script>

<style lang="less" scoped>
  .article_detail{
    margin: 10px 40px;

  }
  .title{
    margin-top: 30px;
    font-size: 45px;
    font-weight: bold;
    color: sandybrown;
  }
  .article_detail{
    font-size: 18px;
  }
  .article_footer{
    display: block;
    color: gray;
    font-size: 15px;
    left:80%
  }
  .skip_article{
    cursor: pointer;
    margin-top: 50px;
  }
  .like{
    /*transform: translate(100%,50);*/
    padding-left: 500px;

    /*transform: scale(2);*/
    width: 40px;
    height: 40px;
  }
</style>