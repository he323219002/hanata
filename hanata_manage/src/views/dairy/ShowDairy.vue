<template>
  <div class="editor">
    <el-form ref="dairyForm" :model="dairyForm"  label-width="80px" disabled>
      <div class="part1">
        <el-form-item label="日记标题" prop="title">
          <el-input v-model="dairyForm.title"></el-input>
        </el-form-item>

        <el-form-item label="日记标签">
          <el-select v-model="dairyForm.tid" placeholder="请选择标签">
            <el-option v-for="item in tagOptions" :label="item.name" :value="item.uid" :key="item.uid"></el-option>
          </el-select>
        </el-form-item>


        <el-form-item label="评论开放" prop="commentOpen">
          <el-select v-model="dairyForm.commentOpen" placeholder="请选择">
            <el-option label="是" value="1"></el-option>
            <el-option label="否" value="0"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="日记开放" prop="open">
          <el-select v-model="dairyForm.open" placeholder="请选择">
            <el-option label="是" value="1"></el-option>
            <el-option label="否" value="0"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="是否存储" prop="state">
          <el-select v-model="dairyForm.state" placeholder="请选择">
            <el-option label="直接发表" value="1"></el-option>
            <el-option label="存储草稿" value="0"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="图片上传">
<!--          <el-dialog :visible.sync="dialogVisible">-->
            <img width="100%" v-if="dialogImageUrl" :src="dialogImageUrl" alt="">
            <p v-else>暂无图片</p>
<!--          </el-dialog>-->
        </el-form-item>

      </div>



      <el-form-item prop="content">
        <vue-markdown :source="dairyForm.content"></vue-markdown>
<!--        <div id="mavon">-->
<!--          <mavon-editor v-model="articleForm.content" ref=md />-->
<!--        </div>-->
      </el-form-item>

    </el-form>
  </div>

</template>

<script>
  import {uploadRequest} from "../../utils/api";
  import {postRequest} from "../../utils/api";
  import {picBaseURL} from "../../utils/api";


  export default {
    created() {
      this.getDairyById()
      this.getTags()
    },
    data() {
      return {
        dairyForm: {
          title: '',
          content: '',
          open: '',
          commentOpen: '',
          tid: '',
          state: '',
          coverPic:''
        },
        tagOptions: [],
        dialogImageUrl:'',
        dialogVisible: false,


      }
    },
    methods: {
      async getDairyById(){
        let id = this.$store.state.dairyId;
        let {data:res} = await postRequest('dairy/show',{'id':id});
        this.dairyForm.title=res.data.title
        this.dairyForm.content=res.data.content
        this.dairyForm.open=res.data.open
        this.dairyForm.commentOpen=res.data.commentOpen
        this.dairyForm.state=res.data.state
        this.dairyForm.coverPic=res.data.coverPic
        this.dairyForm.tid=res.data.tagId
        this.dialogImageUrl=picBaseURL+res.data.coverPic
      },

      async getTags() {
        let {data: res} = await postRequest('tag');
        console.log(res);
        if (res.code === 0) {
          this.tagOptions = res.data
        } else {
          this.$router.push({name: 'Login'})
        }
      },

    }
  }
</script>

<style scoped lang="less">
  .part1 {
    /*font-weight: bold;*/
    width: 50%;;
  }

</style>