<template>

  <div class="table">

    <template>
      <el-button type="primary" style="float: left;margin: 2px 2px" @click="handleNew">新增</el-button>

      <div class="select">
        <el-select v-model="state" placeholder="请选择">
          <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
          </el-option>
        </el-select>
      </div>



      <el-table
              :data=article.list
              style="width: 100%"
              @row-click="getArticleId"
      >
        <el-table-column
                label="标题"
                prop="title"
        >
        </el-table-column>
        <el-table-column
                label="点赞"
                prop="like">
        </el-table-column>
        <el-table-column
                label="评论数"
                prop="commentCount">
        </el-table-column>
        <el-table-column
                label="点击数"
                prop="viewCount">
        </el-table-column>
        <el-table-column label="是否发送">
          <template slot-scope="scope">
            <p v-if="scope.row.state==='1'">已发送</p>
            <p v-else>草稿</p>
          </template>
        </el-table-column>
        <el-table-column
                label="日期"
                prop="createTime">
        </el-table-column>
        <el-table-column
                align="right">
          <template slot="header" slot-scope="scope">

            <el-input
                    v-model="search"
                    size="mini"
                    placeholder="搜索"/>
          </template>


          <template slot-scope="scope">
            <el-button
                    size="mini"
                    @click="handleEdit(scope.$index, scope.row)"
                    @click.stop.native>编辑
            </el-button>

            <el-popconfirm
                    title="确定删除吗？"
                    @onConfirm="handleDelete(scope.$index, scope.row)"
                    @click.stop.native
            >
            <el-button
                    slot="reference"
                    size="mini"
                    type="danger">删除
                    <!--@click="handleDelete(scope.$index, scope.row)">删除-->
            </el-button>
            </el-popconfirm>
          </template>

        </el-table-column>

      </el-table>

      <div class="block">
        <span class="demonstration"></span>
        <el-pagination
                layout="prev, pager, next"
                :total=article.total
                @current-change="handleCurrentChange"
        >
        </el-pagination>
      </div>

    </template>
  </div>

</template>
<script>
  import {postRequest} from "../../utils/api"
  import {AsyncComponentPromise as resolve} from "vue";
  import {checkToken} from "../../utils/utils";

  export default {
    name: "Article",
    created() {
      this.cid=this.$route.query.cid;
      this.lid=this.$route.query.lid;
      this.getArticle();
    },
    data() {
      return {
        article: [],
        search:'',
        state:'',
        cid:'',
        lid:'',
        options:[
          {value:'', label:'全部'},
          {value:'1',label:'已发送'},
          {value:'0',label:'草稿'}
        ]
      }
    },
    methods: {
      async getArticle() {
        let {data: res} = await postRequest("article",{"admin":"1",'cid':this.cid,'lid':this.lid,'q':this.search,'state':this.state});

        checkToken(res)
        this.article = res.data;

      },
      handleNew(){
        this.$router.push({name:"NewArticle"})
      },
      handleEdit(index, row) {
        let articleId = row.uid
        let payload = {
          'id':articleId
        }
        this.$store.commit('setArticleData',payload)
        this.$router.push({name:'UpdateArticle'})
      },



      getArticleId(row,column,event){
        let articleId = row.uid
        let payload = {
          'id':articleId
        }
        this.$store.commit('setArticleData',payload)
        this.$router.push({name:'ShowArticle'})
      },

      async handleDelete(index, row) {

        let {data:res} = await postRequest('article/del',{'id':row.uid});
        if (res.code===0){
          this.$router.go(0)
        } else {
          console.log(res.data());
        }
      },
      async handleCurrentChange(val){
        let Pageparams = {
          "limit": val+'',
          "admin": "1",
          'q':this.search,
          "cid": this.cid,
          'lid': this.lid
        }
        let {data:res} = await postRequest("article",Pageparams);
        if (res.code === 0) {
          console.log(res.data);
          this.article = res.data;
          // return []
          // console.log(this.article);
        }
      }
    },
    watch:{
      search: async function(newVal,oldVal){

        let pageParams = {
          "admin": "1",
          'q':this.search,
          "cid": this.cid,
          'lid': this.lid,
          'state':this.state

        }
        let {data:res} = await postRequest("article",pageParams);
        if (res.code === 0) {
          console.log(res.data);
          this.article = res.data;
          // return []
          // console.log(this.article);
        }
      },
      state: async function(newVal,oldVal){
        let pageParams = {
          "admin": "1",
          'q':this.search,
          "cid": this.cid,
          'lid': this.lid,
          'state':this.state
        }
        let {data:res} = await postRequest("article",pageParams);
        if (res.code === 0) {
          console.log(res.data);
          this.article = res.data;
          // return []
          // console.log(this.article);
        }
      }
    },

  }
</script>

<style lang="less" scoped>
  .select{
    float: left;
    margin: 2px;
  }
</style>