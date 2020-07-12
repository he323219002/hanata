<template>
  <div class="index">
    <div class="bgp" ref="element">
      <img :src="require('../assets/jakpost.travel-cherry-blossom-wallpaper-2439924.jpg')" width="100%">
    </div>
    <div class="main_container">
      <div class="container article_list">
        <v-article></v-article>
      </div>
      <div class="container tag_list">
        <v-tag></v-tag>
      </div>
    </div>
  </div>
</template>

<script>
  import Article from "../components/Article"
  import Tag from "../components/Tag"


  export default {
    name: "index",
    mounted() {
      this.calcHight();
    },
    methods:{
      calcHight() {
        const that = this;
        let element = that.$refs.element;

        that.$nextTick(() => {
          console.log(element.offsetHeight); // 600
        });

      }
    },
    components:{
      'v-article':Article,
      'v-tag':Tag
    }
  }
</script>

<style scoped lang="less">
  .main_container{
    width: 100%;
    height: 1000px;
    display: flex;
    /*position: absolute;*/
  }
  .container{
    width: 100%;
    height: 100%;
    background-color: black;
  }
  .article_list{
    flex:1;
    width: 80%;
    overflow: auto;
    background-color: black;
    border: 2px solid gray;
    box-shadow: white;
    border-radius: 10px;
    margin-right: 3px;

  }
  .tag_list{
    width: 20%;
    height: 100%;
    background-color: black;
    border: 2px solid gray;
    box-shadow: white;
    border-radius: 10px;
  }



</style>