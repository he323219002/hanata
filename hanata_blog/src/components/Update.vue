<template>
  <div class="update">
    <div class="container infinite-list-wrapper"
         v-infinite-scroll="load"
         infinite-scroll-disabled="disabled"
         infinite-scroll-distance="20">
      <div class="per_update"

           v-for="item in update">
        <div class="left">
          <div class="title">
            V：{{item.version}}
          </div>

          <div class="createTime">
            posted:{{item.createTime}}
          </div>
        </div>

        <div class="right">
          <div class="content">
            更新：{{item.content}}
          </div>
        </div>

        <div class="link-line"></div>
      </div>

      <p v-if="loading">加载中...</p>
      <p v-if="noMore">没有更多了</p>
    </div>
  </div>
</template>

<script>
  import {postRequest} from "../utils/api";
  import {checkToken} from "../utils/utils";

  export default {
    name: "Update",
    created() {
      this.getUpdate()
    },
    methods:{
      async getUpdate(){
        let {data:res} = await postRequest('update',{'limit':this.limit+'','offset':'2'});
        checkToken(res)
        this.update=res.data.list
        this.total=res.data.total
        this.pages=res.data.pages
        this.count=2
      },
      async load(){
        this.loading=true
        this.limit += 1
        // console.log(this.count);
        // console.log(this.total);

        if (this.limit <= this.pages){
          let {data:res} = await postRequest('update',{'limit':this.limit+'','offset':'2'});

          setTimeout(()=>{
            this.update=this.update.concat(res.data.list)
            this.count += res.data.list.length
            this.count
            console.log(this.count);
            this.loading=false
          },3000)
        }
        else{
          this.loading=false
        }
      }
    },
    data(){
      return{
        update:[],
        total:0,
        count:0,
        loading:false,
        limit:1,
        pages:''
      }
    },
    computed:{
      noMore(){
        return this.count >= this.total
      },
      disabled(){
        return this.loading || this.noMore
      }
    },



  }
</script>

<style lang="less" scoped>
  .container{
    text-align: center;
    height: 500px;
    width: 50%;
    transform: translate(50%);
    /*border: 1px solid white;*/
    overflow: auto;
  }
  .link-line{
      width: 50%;
      height: 100px;
      border-right: solid #409EFF 2px;
  }
  .per_update{
    /*position: absolute;*/
  }
  .left{
    /*position: relative;*/
    transform: translate(-20%,50%);
  }
  .right{
    transform: translate(20%,-50%);
  }
  .title{
    font-size: 25px;
    color: gold;
  }
  .createTime{
    color: gray;
    margin-top: 25px;
  }
  .content{
    font-size: 18px;
    color: wheat;
  }
</style>