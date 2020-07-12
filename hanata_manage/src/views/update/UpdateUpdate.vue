<template>
  <div class="updateUpdate">
    <el-form ref="updateForm" :model="updateForm" :rules="rules" label-width="80px">
      <el-form-item label="版本号" prop="version" >
        <el-input  v-model="updateForm.version"></el-input>
      </el-form-item>

      <el-form-item label="更新内容" prop="version" >
        <el-input  v-model="updateForm.content"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="updateUpdate">更新</el-button>
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
      this.getUpdate()
    },
    name: "UpdateUpdate",
    data() {
      return {
        updateForm:{
          id:'',
          version:'',
          content:''
        },
        rules:{
          version:[{
            required:true,
            message:'版本号必填',
            trigger:'blur'
          }],
          content:[{
            required:true,
            message:'更新内容必填',
            trigger:'blur'
          }]
        }
      }
    },
    methods:{
      getUpdate(){
        this.updateForm.id = this.$store.state.update.id
        this.updateForm.version = this.$store.state.update.version
        this.updateForm.content = this.$store.state.update.content
      },
      async updateUpdate(){
        let {data:res} = await postRequest('update/update',this.updateForm);
        console.log(res);
        checkToken(res)
        this.$router.push({name:'Update'})
      }
    }
  }
</script>

<style scoped>
  .updateUpdate{
    margin: 5px;
  }
</style>