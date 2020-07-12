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
        target:'1',
        targetId:''

      }
    },
    methods:{
      async submit(id,kind){

        let {data:res} = await postRequest('comment/new',{'articleId':id,'content':this.content,'fartherId':this.fatherId,'kind':kind,'target':this.target,'targetId':this.targetId});
        checkToken(res)
        this.$router.go(0)
      },
      focus(username,fatherId,targetId){
        this.placeholder='回复： ' + username
        this.content = '回复'+username +':'
        this.$refs.focus_input.focus()
        this.$notify({
          title:'直接输入回复内容即可',
          position:'bottom-right'
        })
        this.fatherId=fatherId
        this.target = '2'
        if (targetId){
          this.targetId=targetId
        } else {
          this.targetId=fatherId
        }

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