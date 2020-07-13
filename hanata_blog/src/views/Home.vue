<template>
  <div class="home">

    <el-container>
      <el-header>
        <div class="navigation">


          <el-menu
                  :default-active="activeIndex2"
                  class="el-menu-demo"
                  mode="horizontal"
                  @select="handleSelect"
                  background-color="#000000"
                  text-color="#fff"
                  :router="true"
                  active-text-color="#ffd04b">
            <el-row>
              <el-col :span="1">
                <div style="width: 60px;height: 60px;float: left">
                  <img :src="require('../assets/fish_icon.png')" style="height: 100%">
                </div>
              </el-col>

              <el-col :span="1" :offset="1">
                <p @click="goIndex" style="cursor: pointer;">Hanata.top</p>
              </el-col>

              <el-col :span="2" :offset="1">
                <el-menu-item index="1" @click="goArticle">技术文章</el-menu-item>
              </el-col>

              <el-col :span="3">
                <el-submenu index="2">
                  <template slot="title">我的随记</template>
                  <div class="item" v-for="item in tag">
                    <el-menu-item  :index=tagUrl+item.uid v-if="item.uid">{{item.name}}</el-menu-item>
                    <el-menu-item  index="/dairy"  v-else>{{item.name}}</el-menu-item>
                  </div>

                </el-submenu>
              </el-col>

              <el-col :span="1" :offset="1">
                <el-menu-item style="padding: 0">
                  <a href="https://github.com/he323219002"><img :src="require('../assets/github5.png')" style="height: 60%"></a>
                </el-menu-item>
              </el-col>
              <el-col :span="1.5" :offset="1.5">
                <el-menu-item  style="padding-left: 20px">
                  <el-popover
                          placement="bottom"
                          title="我的Telegram"
                          width="200"
                          trigger="click"
                          content="jimmy_1017">
                    <img :src="require('../assets/tele5.png')" slot="reference" style="height: 80%;background-color: white">
<!--                    <el-button slot="reference">click 激活</el-button>-->
                  </el-popover>

                </el-menu-item>
              </el-col>
              <el-col :span="4">
                <p>Play in the right way</p>
              </el-col>
              <el-col :span="3">
                <el-input
                        placeholder="请输入内容"
                        prefix-icon="el-icon-search"
                        style="margin-top: 10px"
                        v-model="esKeyword"
                        @keyup.enter.native=search()
                >
                </el-input>
              </el-col>
              <el-col :span="1" :offset="1">
                <p style="font-size: 15px;margin-top: 16px;cursor: pointer" v-if="state==='1'" @click="exit">注销</p>
                <p style="font-size: 15px;margin-top: 16px;cursor: pointer" v-else @click="login">登录</p>

              </el-col>

              <el-col :span="2">
                <el-submenu index="3">
                  <template slot="title">更多</template>
                  <el-menu-item @click="goUpdate">更新记录</el-menu-item>
                  <el-menu-item @click="goMessage" v-if="state==='1'">我的消息</el-menu-item>
                  <el-menu-item @click="subscribe">订阅本站</el-menu-item>
                  <el-menu-item @click="goAccount" v-if="state==='1'">个人中心</el-menu-item>
                </el-submenu>

              </el-col>
            </el-row>

          </el-menu>
        </div>
      </el-header>


      <el-main>
        <router-view></router-view>
      </el-main>

      <el-footer style="height: 100px">

        <div class="link-top"></div>
        <p>Copyright © 2020 hanata.top</p>
        <p>Inspired by go.kieran.top</p>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
  // @ is an alias to /src
  import Index from '../components/Index'
  import {getToken, postRequest} from "../utils/api";
  import {checkToken} from "../utils/utils";
  let mail = window.sessionStorage.getItem("mail");

  export default {

    created() {
      this.getState();
      this.getDairyTag();
      this.esKeyword=this.$route.query.q
    },
    name: 'Home',
    data() {
      return {
        activeIndex: '1',
        activeIndex2: '1',
        state: "1",
        tag: [],
        tagUrl:'/dairy?tagId=',
        esKeyword:''
      }
    },
    methods: {
      handleSelect(key, keyPath) {
        // console.log(key, keyPath);
      },
      goIndex() {
        this.$router.push({path: '/index'})
      },
      goArticle() {
        this.$router.push({name: 'Article'})
      },
      goUpdate(){
        this.$router.push({name:'Update'})
      },
      goMessage(){
        this.$router.push({name:'Message'})
      },
      goAccount(){
        this.$router.push({name:"Account"})
      },

      async subscribe(){
        let {data:res} = await postRequest('subscribe');
        // checkToken(res)
        if (res.code===0){
          this.$notify({
            title:'您的'+mail+'已经成功订阅'
          })
        }
        else{
          this.$message({
            message: '请先登录',
            type: 'warning'
          })
        }

      },

      exit() {
        window.sessionStorage.clear();
        this.$router.go(0)
      },
      login() {
        this.$router.push({name: 'Login'})
      },
      async getDairyTag() {
        let {data: res} = await postRequest('tag');
        checkToken(res);
        this.tag = res.data
        this.tag.push({'name':'所有选项','uid':''})
      },
      getState() {
        let token = getToken();
        if (token) {
          this.state = '1'
        } else {
          this.state = ''
        }
      },
      async search(){
        this.$router.push({name:'ESSearch',query:{"q":this.esKeyword}})
      }

    },
    components: {}
  }
</script>

<style lang="less">
  .home {
    background-color: #000000;
    color: #eeeeee;

    .el-header {
      padding: 0;
    }
  }

  .home .el-main {
    padding: 0;
  }

  .navigation {
    height: 60px;
  }

  .home .header_pic1 {
    height: 50px;
    width: 50px;
  }

  .link-top {
    width: 100%;
    height: 1px;
    margin-top: 5px;

    border-top: solid gray 1px;
  }

  .el-footer {
    text-align: center;
  }


</style>
