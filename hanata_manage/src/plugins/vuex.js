import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export let store = new Vuex.Store({
  state: {
    articleId: '',
    categoryId: '',
    categoryName: '',
    labelId: '',
    labelName: '',
    dairyId: '',
    tagId:'',
    tagName:'',
    update:{
      id:'',
      version:'',
      content:''
    }
  },
  mutations: {
    setArticleData(state, payload) {
      state.articleId = payload.id
    },
    setCategoryData(state, payload) {
      state.categoryId = payload.id
      state.categoryName = payload.name
    },
    setLabelData(state, payload) {
      state.labelId = payload.id
      state.labelName = payload.name
    },
    setDairyData(state, payload) {
      state.dairyId = payload.id
    },
    setTagData(state, payload) {
      state.tagId = payload.id
      state.tagName = payload.name
    },
    setUpdateData(state,payload){
      state.update.id = payload.id
      state.update.version = payload.version
      state.update.content = payload.content
    }
  },
})