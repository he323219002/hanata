import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import {Message} from "element-ui";
import vueMarkdown from 'vue-markdown'
import VueStar from 'vue-star'

Vue.component('VueStar',VueStar)


Vue.prototype.$message=Message
Vue.use(ElementUI);
Vue.component('vue-markdown',vueMarkdown)


Vue.config.productionTip = false

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
  router,
  store,
  render: h => h(App)
}).$mount('#app')
