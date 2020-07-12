import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/index',
  },
  {
    path: '/',
    name: 'Home',
    component: Home,
    children: [
      {path: '/index', name: 'Index', component: () => import('../components/Index')},
      {path: '/article', name: 'Article', component: () => import('../components/Article')},
      {path: '/dairy', name: 'Dairy', component: () => import('../components/Dairy')},
      {path:'/article/detail',name:'ArticleDetail',component:()=>import('../components/ArticleDetail')},
      {path:'/dairy/detail',name:'DairyDetail',component:()=>import('../components/DairyDetail')},
      {path:'/search',name:'ESSearch',component:()=>import('../components/ESSearch')},
      {path:'/update',name:'Update',component:()=>import('../components/Update')},
      {path:'/message',name:'Message',component:()=>import('../components/Message')},
      {path:'/account',name:'Account',component:()=>import('../components/Account')},
      {path:'/redirect/resetPwd',name:'ResetPassword',component:()=>import('../components/ResetPassword')},
    ]
  },
  {path: '/login', name: 'Login', component: () => import('../components/Login')},
  {path: '/register', name: 'Register', component: () => import('../components/Register')},
  {path:'/404',name:'Error',component:()=>import('../components/Error')},
  {
    path: '*',
    redirect: '/404'
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
