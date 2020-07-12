<template>
  <div class="editor">
    <el-form ref="articleForm" :model="articleForm" :rules="rules" label-width="80px">
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
                    :label="item.name"
                    :value="item.uid">
            </el-option>
          </el-select>
        </el-form-item>
      </div>


      <el-form-item prop="content">
        <div id="mavon">
          <mavon-editor v-model="articleForm.content" ref=md @imgAdd="imgAdd" @imgDel="imgDel"/>
        </div>
      </el-form-item>


      <el-form-item>
        <el-button type="primary" @click="checkFormAndCreate">创建</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </div>

</template>

<script>
  import {uploadRequest} from "../../utils/api";
  import {postRequest} from "../../utils/api";
  import {picBaseURL} from "../../utils/api";
  import {checkToken} from "../../utils/utils";


  export default {
    created() {

      this.getLabels();
      this.getCategories();

      if(this.$route.path==="/article/update"){
        this.getArticleById();
      }

    },
    data() {
      return {
        articleForm: {
          title: '',
          content: '',
          open: '1',
          commentOpen: '1',
          cid: '',
          lid: [],
          state: '1',
        },
        img_file: [],
        categoryOptions: [],
        labelOptions: [],
        updateLabelOptions:[],
        rules: {
          title: [{
            required: true,
            message: '标题必填',
            trigger: 'blur'
          }],

          commentOpen: [{
            required: true,
            message: '该项必填',
            trigger: 'blur'
          }],
          open: [{
            required: true,
            message: '该项必填',
            trigger: 'blur'
          }],
          state: [{
            required: true,
            message: '标题必填',
            trigger: 'blur'
          }],
          content: [{
            required: true,
            message: '标题必填',
            trigger: 'blur'
          }],
        }
      }
    },
    methods: {
      // 绑定@imgAdd event
      async imgAdd(pos, $file) {
        // 第一步.将图片上传到服务器.
        var formdata = new FormData();
        formdata.append('file', $file);
        formdata.append('fileType', "pic")
        this.img_file[pos] = $file;
        let {data: res} = await uploadRequest('upload/', formdata);
        console.log(picBaseURL + res.data.filePath);
        this.$refs.md.$img2Url(pos, picBaseURL + res.data.filePath)
      },
      imgDel(pos) {
        delete this.img_file[pos];
      },
      async getCategories() {
        let {data: res} = await postRequest('category');
        // console.log(res);
        if (res.code === 0) {
          this.categoryOptions = res.data
          this.categoryOptions.push({'uid':'','name':'请选择分类'})
        } else {
          this.$router.push({name: 'Login'})
        }
      },

      async getLabels() {
        let {data: res} = await postRequest('label');
        if (res.code === 0) {
          this.labelOptions = res.data
          console.log(this.labelOptions);
        } else {
          this.$router.push({name: 'Login'})
        }
      },

      checkFormAndCreate() {
        this.$refs.articleForm.validate(valid => {
          if (!valid) {
            this.$message({
              message:'表单错误',
              type:'warning'
            })
          } else {
            this.submitArticle()
          }
        })
      },

      checkFormAndUpdate() {
        this.$refs.articleForm.validate(valid => {
          if (!valid) {
            this.$message({
              message:'表单错误',
              type:'warning'
            })
          } else {
            this.updateArticle()
          }
        })
      },


      async submitArticle() {
        let {data: res} = await postRequest('article/new', this.articleForm);
        if (res.code === 0) {
          this.$router.push({name: 'Article'})
        } else {
          this.$router.push({name: 'Login'})
        }
      },

      async updateArticle() {
        let {data: res} = await postRequest('article/update', this.articleForm);
        if (res.code === 0) {
          this.$router.push({name: 'Article'})
        } else {
          this.$router.push({name: 'Login'})
        }
      },

      async getArticleById(){
        let id = this.$store.state.articleId;
        let {data:res} = await postRequest('article/show',{'uid':id});
        console.log(res);
        checkToken(res)
        if (res.code===0){
          this.articleForm.title=res.data.title
          this.articleForm.content=res.data.content
          this.articleForm.open=res.data.open
          this.articleForm.commentOpen=res.data.commentOpen
          this.articleForm.cid=Boolean(res.data.cid)?res.data.cid:''
          this.articleForm.state=res.data.state
          if(res.data.label){
            res.data.label.forEach((a)=>{
              this.articleForm.lid.push(a.uid)
            })
          }
        }

      }
    }
  }
</script>

<style scoped lang="less">
  .part1 {
    width: 50%;;
  }

</style>