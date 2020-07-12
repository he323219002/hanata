<template>
  <div class="search">
    <div class="per_res" v-for="item in res.data" @click="skipTo(item.type,item.id)">
      <div class="title" v-html="item.title">
        标题：{{item.title}}
      </div>

      <div class="content" v-html="item.content">
        内容:  {{item.content}}
      </div>

      <div class="create_time">
        创建时间 {{item.createTime}}
      </div>
    </div>

    <div class="pagination" style="transform: scale(1.2);text-align: center">
      <el-pagination
              style="height: 100%"
              background
              :page-size="20"
              :pager-count="11"
              layout="prev, pager, next"
              :total="res.total"
              @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import {postRequest} from "../utils/api";

  export default {
    name: "ESSearch",
    created(){
      this.getESRes()
    },
    data(){
      return{
        res:'',
        keyword:'',
      }
    },
    methods:{
      async getESRes(){
        let q = this.$route.query.q;
        this.keyword=q
        let {data:res} = await postRequest('search',{'keyword':this.keyword});
        console.log(res);
        this.res = res.data
      },
      async handleCurrentChange(val){
        let Pageparams = {
          'keyword':this.keyword,
          "page": val+'',
        }
        let {data:res} = await postRequest("search",Pageparams);
        console.log(res);
        this.res = res.data;
      },
      skipTo(type,id){
        if (type==='1'){
          this.$router.push({name:'ArticleDetail',query:{'id':id}})
        }
        else if(type==='2'){
          this.$router.push({name:"DairyDetail",query:{'id':id}})
        }
      }
    },
    watch:{
      '$route'(to,from){
        if (to.query.q !== from.query.q){
          this.keyword=to.query.q;
          this.getESRes();
        }
      }
    }
  }
</script>

<style lang="less" scoped>

  .title{
    margin-top: 20px;
    font-size: 25px;
    font-weight: normal;
    color: dodgerblue;
  }
  .content{
    background-color: #000000;
  }

  /deep/ .highlight{
    color:red;
    font-weight: bolder;
  }
  .create_time{
    font-size: 12px;
    margin-top: 20px;
  }

  .per_res{
    margin: 20px;
    border-top:1px dashed #eeeeee; width:100%
  }
</style>