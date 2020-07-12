<template>
  <div class="tag">
    <p class="describe">标签</p>
    <div class="article-label part" >
      <el-tag class="per" type="info" v-for="item in label" @click="skipArticleByLabel(item.uid)">{{item.name}}</el-tag>
    </div>

    <div class="space"></div>

    <p class="describe">分类</p>
    <div class="article-category part" >
      <el-tag class="per" type="info" v-for="item in category" @click="skipArticleByCategory(item.uid)">{{item.name}}</el-tag>
    </div>
  </div>
</template>

<script>
  import {postRequest} from "../utils/api";

  export default {
    name: "Tag",
    created() {
      this.initTag()
    },
    methods:{
      async initTag(){
        let {data:labelRes} = await postRequest('label');
        let {data:categoryRes} = await postRequest('category');
        this.label=labelRes.data
        this.category=categoryRes.data
      },
      skipArticleByLabel(id){
        this.$router.push({name:"Article",query:{'label':id}})
      },
      skipArticleByCategory(id){
        this.$router.push({name:"Article",query:{'category':id}})

      }
    },
    data(){
      return{
        label:'',
        category:''
      }
    }
  }
</script>

<style lang="less" scoped>
  .describe{
    text-align: center;
  }
  .part{
    /*border: 1px solid #dddddd;*/
    font-size: 13px;
  }
  .per{
    margin: 8px;
    cursor: pointer;
  }
  .space{
    height: 20px;
  }
</style>