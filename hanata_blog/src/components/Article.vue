<template>
  <div class="article">
    <div class="per_article" v-for="item in article.list">
      <div class="article_title">
        <p @click="showDetail(item.uid)">{{item.title}}</p>
      </div>

      <div class="article_content">
        摘要：{{item.content}}  <a class="read_all" @click="goDetail(item.uid)">阅读全文</a>
      </div>
      <div class="space"></div>


      <el-row style="padding-bottom: 5px">
        <el-col :span="5">
          <div class="article_createTime">
            发布时间  {{item.createTime}}
          </div>
        </el-col>


        <el-col :span="2" :offset="8">
          <div>
            阅读数{{item.viewCount}}
          </div>
        </el-col>
        <el-col :span="2" :offset="1">
          <div>
            评论数{{item.commentCount}}
          </div>
        </el-col>

      </el-row>

    </div>
    <div class="pagination" style="transform: scale(1.2)">
      <el-pagination
              style="height: 100%"
              background
              :page-size="10"
              :pager-count="11"
              layout="prev, pager, next"
              :total="article.total"
              @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>

  </div>

</template>

<script>
  import {postRequest} from "../utils/api";
  import {checkToken} from "../utils/utils";

  export default {
    created(){
      this.getArticle();

    },
    name: "Article",
    data(){
      return {
        articleForm:{
          lid:'',
          cid:'',
          limit:'20',
        },
        article:'',

      }
    },
    methods:{
      async getArticle(){
        let label = this.$route.query.label;
        let category = this.$route.query.category;
        if (label){
          this.articleForm.lid=label
        }
        if (category){
          this.articleForm.cid=category
        }
        let {data:res} = await postRequest('article',this.articleForm);
        this.article = res.data
      },

      goDetail(id){
        this.$router.push({name:"ArticleDetail",query:{"id":id}})
      },
      async handleCurrentChange(val){
        let Pageparams = {
          "limit": val+'',
          "cid": this.cid,
          'lid': this.lid
        }
        let {data:res} = await postRequest("article",Pageparams);
        this.article = res.data;
      },
      showDetail(id){
        this.$router.push({path:'/article/detail',query:{id:id}})
      }
    }
  }
</script>



<style lang="less" scoped>
  a{
    text-decoration: none;
  }
  .article{
    text-align: left;
    margin: 20px;

  }
  .per_article{
    border-top:1px dashed #eeeeee; width:100%
  }
  .article_title{
    cursor: pointer;
    font-size: 30px;
    color: sandybrown;
    font-weight: bold;
  }
  .article_content{
    font-weight: 500;
  }
  .space{
    height: 30px;
  }
  .read_all{
    cursor: pointer;
    color: saddlebrown;
  }
  .pagination{
    height: 60px;
    width: 100%;
    text-align: center;
    margin-top: 30px;
  }

</style>