<template>
  <div class="editor">
    <el-form ref="dairyForm" :model="dairyForm" :rules="rules" label-width="80px">
      <div class="part1">
        <el-form-item label="文章标题" prop="title">
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

        <el-form-item label="文章开放" prop="open">
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
          <el-upload
                  :action="uploadUrl"
                  list-type="picture-card"
                  :limit="1"
                  :on-preview="handlePictureCardPreview"
                  :on-remove="handleRemove"
                  :auto-upload="true"
                  :data="extraData"
                  name="file"
                  :file-list="fileList"
                  :headers="token"
                  :on-exceed="onExceed"
                  :on-success="finishUpload">
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-form-item>


      </div>


<!--      <el-form-item prop="content">-->
<!--        <div id="mavon">-->
<!--          <mavon-editor v-model="dairyForm.content" ref=md @imgAdd="imgAdd" @imgDel="imgDel"/>-->
<!--        </div>-->
<!--      </el-form-item>-->

      <el-form-item label="内容">
        <div class="tinymce-box">
          <editor v-model="dairyForm.content"
                  :init="editorInit"
                  :disabled="disabled">
          </editor>
        </div>
      </el-form-item>



      <el-form-item>
        <el-button type="primary" @click="checkFormAndUpdate">更新</el-button>
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
  import {getToken} from "../../utils/utils";
  import {uploadUrl} from "../../../config/hanataConfig"
  import tinymce from 'tinymce/tinymce' //tinymce默认hidden，不引入不显示
  import Editor from '@tinymce/tinymce-vue'
  import 'tinymce/themes/silver'
  import 'tinymce/plugins/image'// 插入上传图片插件
  import 'tinymce/plugins/link'// 插入上传图片插件
  import 'tinymce/plugins/imagetools'
  import 'tinymce/plugins/media'// 插入视频插件
  import 'tinymce/plugins/paste'// 插入视频插件

  import 'tinymce/plugins/table'// 插入表格插件
  import 'tinymce/plugins/lists'// 列表插件
  import 'tinymce/plugins/code'// 列表插件
  import 'tinymce/plugins/colorpicker'// 列表插件
  import 'tinymce/plugins/textcolor'// 列表插件
  import 'tinymce/plugins/contextmenu'// 列表插件
  import 'tinymce/plugins/wordcount'// 字数统计插件
  import 'tinymce/icons/default/icons'

  export default {
    created() {
      this.getDairyById()
      this.getTags();
    },
    data() {
      const $stripTags = (str, allowed_tags) => {
            let key = '', allowed = false
            let matches = []
            let allowed_array = []
            let allowed_tag = ''
            let i = 0
            let k = ''
            let html = ''
            let replacer = function (search, replace, str) {
              return str.split(search).join(replace)
            }
            // Build allowes tags associative array
            if (allowed_tags) {
              allowed_array = allowed_tags.match(/([a-zA-Z0-9]+)/gi)
            }
            str += ''

            // Match tags
            matches = str.match(/(<\/?[\S][^>]*>)/gi)
            // Go through all HTML tags
            for (key in matches) {
              if (isNaN(key)) {
                // IE7 Hack
                continue
              }

              // Save HTML tag
              html = matches[key].toString()
              // Is tag not in allowed list? Remove from str!
              allowed = false

              // Go through all allowed tags
              for (k in allowed_array) {            // Init
                allowed_tag = allowed_array[k]
                i = -1

                if (i != 0) {
                  i = html.toLowerCase().indexOf('<' + allowed_tag + '>')
                }
                if (i != 0) {
                  i = html.toLowerCase().indexOf('<' + allowed_tag + ' ')
                }
                if (i != 0) {
                  i = html.toLowerCase().indexOf('</' + allowed_tag)
                }

                // Determine
                if (i == 0) {
                  allowed = true
                  break
                }
              }
              if (!allowed) {
                str = replacer(html, "", str) // Custom replace. No regexing
              }
            }
            return str
          }

      return {
        dairyForm: {
          id:this.$store.state.dairyId,
          title: '',
          content: '',
          open: '',
          commentOpen: '',
          tid: '',
          state: '',
          coverPic:'',
          picId:''
        },
        editorInit:{
          language_url: '/tinymce/langs/zh_CN.js',
          language: 'zh_CN',
          paste_remove_styles: true,
          skin_url: '/tinymce/skins/ui/oxide',
          // skin_url: 'tinymce/skins/ui/oxide-dark',//暗色系
          height: 300,
          plugins: "link lists image code table colorpicker textcolor wordcount contextmenu paste",
          toolbar: "bold italic underline strikethrough | fontsizeselect | forecolor backcolor | alignleft aligncenter alignright alignjustify|bullist numlist |outdent indent blockquote | undo redo | link unlink image code | removeformat",
          branding: false,
          menubar: false,
          paste_as_text:true,
          paste_auto_cleanup_on_paste : true,
          paste_remove_styles_if_webkit: true,
          paste_strip_class_attributes: true,
          paste_preprocess: (pl, o) => {
            o.content = $stripTags(o.content, "sup,sub");
          },
          images_upload_handler: async (blobInfo, success, failure) => {

            const file = blobInfo.blob();

            var formdata = new FormData();
            formdata.append('file', file);
            formdata.append('fileType', "pic")
            let {data: res} = await uploadRequest('upload/', formdata);
            if (res.code===0){
              let imgPath = picBaseURL + res.data.filePath;
              success(imgPath)
            } else{
              failure('failure upload')
            }
          }
        },
        fileList:[],

        extraData: {
          'fileType': 'pic',
        },
        token: {
          'Authorization': getToken()
        },
        dialogImageUrl:'',
        dialogVisible: false,
        disabled: false,
        uploadUrl: uploadUrl,
        img_file: [],
        tagOptions:[],
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

      async getTags() {
        let {data: res} = await postRequest('tag');
        if (res.code === 0) {
          this.tagOptions = res.data
          this.tagOptions.push({'uid':'','name':'请选择分类'})

        } else {
          this.$router.push({name: 'Login'})
        }
      },


      checkFormAndUpdate() {
        this.$refs.dairyForm.validate(valid => {
          if (!valid) {
            this.$message({
              message:'表单错误',
              type:'warning'
            })

          } else {
            this.updateDairy()
          }
        })
      },



      async updateDairy() {
        let {data: res} = await postRequest('dairy/update', this.dairyForm);
        console.log(this.dairyForm);
        if (res.code === 0) {
          this.$router.push({name: 'Dairy'})
        }
        else if (res.msg === '请上传封面'){
          this.$message({
            message: res.msg,
            type: 'warning'
          })
        }
        else {
          this.$router.push({name: 'Login'})
        }
      },

      async getDairyById(){
        let id = this.$store.state.dairyId;
        let {data:res} = await postRequest('dairy/show',{'id':id});
        console.log(res);
        this.dairyForm.title=res.data.title
        this.dairyForm.content=res.data.content
        this.dairyForm.open=res.data.open
        this.dairyForm.commentOpen=res.data.commentOpen
        this.dairyForm.state=res.data.state
        this.dairyForm.coverPic=res.data.coverPic
        this.dairyForm.picId = res.data.picId
        this.dairyForm.tid=res.data.tagId
        this.dialogImageUrl=picBaseURL+res.data.coverPic
        let uploadFile = {
          "url":this.dialogImageUrl
        }
        console.log(this.fileList);
        this.fileList.push(uploadFile)
        console.log(this.fileList);
      },

      handleRemove(file, fileList) {
        console.log(file, fileList);
      },

      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },


      onExceed(file, fileList) {
        this.$message.error('只能上传一个文件，请先删除已有文件!')
      },

      finishUpload(response, file, fileList) {
        console.log(response);
        this.dairyForm.picId=file.response.data.uid
      },
    },
    components: {'editor': Editor}

  }
</script>

<style scoped lang="less">
  .part1 {
    width: 50%;;
  }


  /deep/ .mce-content-body{
    background-color: transparent;
  }

</style>