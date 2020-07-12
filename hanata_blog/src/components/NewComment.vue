<template>
  <div class="new-comment">
    <div class="content">
      <el-input
              type="textarea"
              ref="focus_input"
              :rows="5"
              :placeholder="placeholder"
              v-model="content">
      </el-input>
      <el-button class="sub-button"
                 type="primary"
                 size="medium"
                 @click="submit(id,kind)"
                 round>提交</el-button>
    </div>
  </div>
</template>

<script>
  import {postRequest} from "../utils/api";
  import {checkToken} from "../utils/utils";

  export default {
    name: "NewComment",
    data() {
      return {
        content: '',
        fatherId:'',
        // replyUsername:'',
        placeholder:'快留下你的评论吧',

      }
    },
    methods:{
      async submit(id,kind){
        console.log(id);
        console.log(kind);
        let {data:res} = await postRequest('comment/new',{'articleId':id,'content':this.content,'fatherId':this.fatherId,'kind':kind});
        checkToken(res)
        console.log(res);
        this.$router.go(0)
      },
      focus(username,fatherId){
        this.placeholder='回复： ' + username
        this.content = '回复： ' + username
        this.$refs.focus_input.focus()
        this.$notify({
          title:'输入回复内容吧',
          position:'bottom-right'
        })
        this.fatherId=fatherId
        console.log(this.fatherId);
      }
    },
    props:['id','kind'],
    watch:{
      content(){
        if (this.content===''){
          this.fatherId=''
          this.placeholder='您现在可以回复博主啦'
        }
      }
    }
  }
</script>

<style lang="less" scoped>
  /deep/.sub-button{
    float: right;
    margin: 20px;
  }

</style>