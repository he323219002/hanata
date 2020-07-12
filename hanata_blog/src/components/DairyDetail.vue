<template>
  <div class="dairy_detail">
    <div class="title">
      {{dairy.title}}
    </div>



    <div class="content" v-html="dairy.content">

    </div>

    <div class="dairy_comment dairy_footer">
      评论数: {{dairy.commentCount}}
    </div>

    <div class="dairy_time dairy_footer">
      post time：{{dairy.createTime}}
    </div>


    <div class="comment">
      <article-comment :id="id"  @commenter="receiveId" ref="child" ></article-comment>
      <new-comment :id="id" :kind="'2'" ref="new_comment"></new-comment>
    </div>

  </div>
</template>

<script>
  import {postRequest} from "../utils/api";
  import {checkToken} from "../utils/utils";
  import articleComment from "../components/Comment"
  import newComment from "../components/NewComment"

  export default {
    name: "DairyDetail",
    data(){
      return{
        dairy:'',
        likeCount:'',
        id:'',
        like:false
      }
    },
    created(){
      this.getDairyById()
    },
    mounted(){
      this.$refs.child.getCommentById(this.id,'2')
    },
    methods:{
      async getDairyById(){
        let id = this.$route.query.id;
        this.id=id
        let {data:res} = await postRequest('dairy/show',{'id':id});
        checkToken(res)
        // console.log(res);
        this.dairy = res.data
      },
      receiveId(username,fid){
        console.log(username);
        this.$refs.new_comment.focus(username,fid)
      },
    },
    watch:{
      '$route'(to,from){
        if (to.query.id !== from.query.id){
          this.getDairyById();
        }
      }
    },
    components:{articleComment,newComment}

  }
</script>

<style lang="less" scoped>
  .dairy_detail{
    margin: 10px 40px;

  }
  .title{
    margin-top: 30px;
    font-size: 45px;
    font-weight: bold;
    color: sandybrown;
  }
  .dairy_detail{
    font-size: 18px;
  }
  .dairy_footer{
    display: block;
    color: gray;
    font-size: 15px;
    left:80%
  }

</style>