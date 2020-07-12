<template>
  <div class="UpdateTag">
    <el-form ref="tagForm" :model="tagForm" :rules="rules" label-width="80px">
      <el-form-item label="标签名称" prop="name" >
        <el-input v-model="tagForm.name"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="updateTag">更新</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import {postRequest} from "../../utils/api";
  import {checkToken} from "../../utils/utils";

  export default {
    created(){
      this.getTag()
    },
    name: "UpdateTag",
    data(){
      return{
        tagForm:{
          id:this.$store.state.tagId,
          name:''
        },
        rules:{
          name:[{
            required:true,
            message:'名称必填',
            trigger:'blur'
          }]
        }
      }
    },
    methods:{
      updateTag(){
        this.$refs.tagForm.validate(valid=>{
          if(!valid){
            this.$message({
              message:'表单错误',
              type:'warning'
            })
          }else{
            this.submitTag()
          }
        })
      },
      async submitTag(){
        let {data:res} = await postRequest('tag/update',this.tagForm);
        checkToken(res)

        if (res.code!==0){
          this.$message({
            message:res.msg,
            type:'warning'
          })
          return
        }
        this.$router.push({name:'_Tag'})
      },

      getTag(){
        this.tagForm.name=this.$store.state.tagName
      }
    }
  }
</script>

<style lang="less" scoped>
  .newCategory{
    margin: 20px;
  }
</style>