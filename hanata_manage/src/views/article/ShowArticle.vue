<template>
  <div class="editor">
    <el-form ref="articleForm" :model="articleForm"  label-width="80px" disabled>
      <div class="part1">
        <el-form-item label="文章标题" prop="title">
          <el-input v-model="articleForm.title"></el-input>
        </el-form-item>
        <el-form-item label="文章类别">
          <el-select v-model="articleForm.cid" placeholder="请选择分类">
            <el-option v-for="item in categoryOptions" :label="item.name" :value="item.uid" :key="item.uid"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="评论开放" prop="commentOpen">
          <el-select v-model="articleForm.commentOpen" placeholder="请选择">
            <el-option label="是" value="1"></el-option>
            <el-option label="否" value="0"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="文章开放" prop="open">
          <el-select v-model="articleForm.open" placeholder="请选择">
            <el-option label="是" value="1"></el-option>
            <el-option label="否" value="0"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="是否存储" prop="state">
          <el-select v-model="articleForm.state" placeholder="请选择">
            <el-option label="直接发表" value="1"></el-option>
            <el-option label="存储草稿" value="0"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="标签类别">
          <el-select v-model="articleForm.lid" multiple placeholder="请选择">
            <el-option
                    v-for="item in labelOptions"
                    :key="item.uid"
                    :label="item.labelName"
                    :value="item.uid">
            </el-option>
          </el-select>
        </el-form-item>
      </div>



      <el-form-item prop="content">
        <vue-markdown :source="articleForm.content"></vue-markdown>
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
      this.getArticleById();
      this.getCategories();
    },
    data() {
      return {
        articleForm: {
          title: '',
          content: '',
          open: '',
          commentOpen: '',
          cid: '',
          lid: [],
          state: '',
        },
        categoryOptions:[],
        labelOptions:[],
      }
    },
    methods: {
      async getArticleById(){
        let id = this.$store.state.articleId;
        let {data:res} = await postRequest('article/show',{'uid':id});
        console.log(res);
        this.labelOptions=res.data.label
        this.articleForm.title=res.data.title
        this.articleForm.content=res.data.content
        this.articleForm.open=res.data.open
        this.articleForm.commentOpen=res.data.commentOpen
        this.articleForm.cid=Boolean(res.data.categoryId)?res.data.categoryId:''
        this.articleForm.state=res.data.state
        if(res.data.label){
          res.data.label.forEach((a)=>{
            this.articleForm.lid.push(a.uid)
          })
        }
      },
      async getCategories() {
        let {data: res} = await postRequest('category');
        if (res.code === 0) {
          this.categoryOptions = res.data
        } else {
          this.$router.push({name: 'Login'})
        }
      },

      getLabels: async function () {
        let {data: res} = await postRequest('label');
        if (res.code === 0) {
          this.labelOptions = res.data
        } else {
          this.$router.push({name: 'Login'})
        }
        // console.log(this.labelOptions);
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