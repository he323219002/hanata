import Vue from 'vue'
import router from './router'
import App from './App'
import '@/plugins/element.js'
import 'element-ui/lib/theme-chalk/index.css'
import '@/plugins/mavonEditor.js'
import '@/plugins/vuex.js'
import '@/plugins/markdown.js'

import {store} from "./plugins/vuex"


Vue.config.productionTip = false;

new Vue({
  created(){
    //在页面加载时读取sessionStorage里的状态信息
    if (sessionStorage.getItem("store") ) {
      this.$store.replaceState(Object.assign({}, this.$store.state,JSON.parse(sessionStorage.getItem("store"))))
    }

    //在页面刷新时将vuex里的信息保存到sessionStorage里
    window.addEventListener("beforeunload",()=>{
      sessionStorage.setItem("store",JSON.stringify(this.$store.state))
    })
  },
  store,
  router,
  components: {App},
  render: h => h(App)
}).$mount('#app');
