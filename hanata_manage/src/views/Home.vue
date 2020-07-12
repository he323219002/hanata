<template>
  <el-container class="home-container">
    <el-header>
      <img class="logo-pic" src="../assets/pngtree-fish-logo-icon-vector-png-image_1127380.jpg">
      <h4 class="hanata-title">后台管理系统</h4>
      <el-button class="exit-button" type="info" @click="exit()">退出登录</el-button>
    </el-header>
    <el-container>
      <el-aside width="200px">

        <el-menu class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" :collapse="isCollapse"
                 background-color="#b3c0d1" :router="true">
          <el-menu-item v-for="item in menu" :index="item.url + ''" :key="item.id">
            <i :class="item.icon"></i>
            <span slot="title">{{item.name}}</span>
          </el-menu-item>
        </el-menu>

      </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>


</template>

<script>
  export default {
    data() {
      return {

        isCollapse: false,
        menu: [
          {name: "技术文章", id: 1, icon: "el-icon-reading", url: "/article"},
          {name: "分类管理", id: 2, icon: "el-icon-document-copy", url: "/category"},
          {name: "label", id: 10, icon: "el-icon-price-tag", url: '/label'},
          {name: "其他文章", id: 3, icon: "el-icon-notebook-1", url: "/dairy"},
          {name: "标签管理", id: 4, icon: "el-icon-collection-tag", url: "/tag"},
          {name: "访问统计", id: 7, icon: "el-icon-s-data", url: "/record"},
          {name: "更新记录", id: 8, icon: "el-icon-share", url: "/update"},
          {name: "主页图片", id: 9, icon: "el-icon-picture", url: "/img"},
          {name: "用户管理", id: 11, icon: "el-icon-user", url: "/user"},
        ]
      };
    },
    methods: {
      handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      },
      exit() {
        window.sessionStorage.clear();
        this.$router.push({name: "Login"})
      },

    }
  }
</script>


<style lang="less" scoped>
  .exit-button {
    float: right;
    margin: 10px;
  }

  .el-main {
    padding: 0;
  }

  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 400px;
    text-align: left;
  }

  .hanata-title {
    float: left;
    line-height: 100%;
    padding-left: 20px;
    letter-spacing: 2px;
  }

  .logo-pic {
    float: left;
    height: 100%;
    width: auto;
  }

  .home-container {
    height: 100%;
  }

  .el-header {
    padding: 0;
    background-color: #001529;
    color: #ffffff;
    line-height: 60px;
  }

  .el-aside {
    background-color: #B3C0D1;
    line-height: 58px;

  }

  .el-main {
    background-color: #f1f2f5;
  }


</style>