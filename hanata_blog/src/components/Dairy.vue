<template>
  <div class="dairy">
    <div v-if="dairy.total===0" style="text-align: center;height: 600px">
      <p>暂无文章，请选择其它分类</p>
    </div>


    <div class="per_dairy" v-for="item in dairy.list" v-else>
      <div class="dairy_title" @click="goDetail(item.uid)">
        <p>{{item.title}}</p>
      </div>

      <el-row>
        <el-col :span="18">
          <div class="dairy_content">
            摘要：{{item.content}}  <a class="read_all" @click="goDetail(item.uid)" style="cursor: pointer">阅读全文</a>
          </div>
          <div class="space"></div>
        </el-col>

        <el-col :span="6">
          <div class="dairy_pic">
            <img :src=baseUrl+item.coverPic style="height: 100%;width: 100%">
          </div>
        </el-col>

      </el-row>



      <el-row style="padding-bottom: 5px">
        <el-col :span="5">
          <div class="dairy_createTime">
            发布时间  {{item.createTime}}
          </div>
        </el-col>


        <el-col :span="2" :offset="3">
          <div>
            阅读数 {{item.viewCount}}
          </div>
        </el-col>
        <el-col :span="2" :offset="1">
          <div>
            评论数 {{item.commentCount}}
          </div>
        </el-col>

      </el-row>

      <div class="pagination"  style="transform: scale(1.2)">
        <el-pagination
                style="height: 100%"
                background
                :page-size="10"
                :pager-count="11"
                layout="prev, pager, next"
                :total="dairy.total"
                @current-change="handleCurrentChange"
        >
        </el-pagination>
      </div>

    </div>

  </div>
</template>

<script>
  import {postRequest} from "../utils/api";
  import {picBaseURL} from "../utils/api";
  import {checkToken} from "../utils/utils";

  export default {
    created(){
      this.getDairy();

    },
    name: "Dairy",
    data(){
      return {
        dairyForm:{
          tid:'',
          limit:'20',
        },
        dairy:'',
        baseUrl:picBaseURL,
      }
    },
    methods:{
      async getDairy(){
        this.dairyForm.tid = this.$route.query.tagId;
        let {data:res} = await postRequest('dairy',this.dairyForm);
        // console.log(res);
        this.dairy = res.data
      },
      async handleCurrentChange(val){
        let Pageparams = {
          "limit": val+'',
          "tid": this.tid,
        }
        let {data:res} = await postRequest("dairy",Pageparams);
        checkToken(res)
        this.dairy = res.data;
      },
      goDetail(id){
        this.$router.push({name:"DairyDetail",query:{"id":id}})
      }
    },
    watch:{
      '$route'(to,from){
        if (to.query.tagId !== from.query.tagId){
          this.dairyForm.tid=to.query.tagId;
          this.getDairy();
        }
      }
    }
  }
</script>

<style lang="less" scoped>
  a{
    text-decoration: none;
  }
  .dairy{
    text-align: left;
    margin: 20px;

  }
  .per_dairy{
    border-top:1px dashed #eeeeee; width:100%
  }
  .dairy_title{
    font-size: 30px;
    color: sandybrown;
    font-weight: bold;
    cursor: pointer;
  }
  .dairy_content{
    font-weight: 500;
  }
  .space{
    height: 30px;
  }
  .read_all{
    color: saddlebrown;
  }
  .pagination{
    height: 60px;
    width: 100%;
    text-align: center;
    margin-top: 30px;
  }
  .dairy_pic{
    width: 200px;
    height: 150px;
  }

</style>