import Vue from 'vue';
import VueRouter from 'vue-router';
import routes from './routes';
import store from '@/store';

Vue.use(VueRouter);

const router = new VueRouter({
    mode: 'history',
    routes,
});

router.beforeEach((to, from, next) => {
    if (from === VueRouter.START_LOCATION) {
        store.dispatch('user/init');
    }

    next();
})

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location)
        .catch(error => {
            if (error.name !== 'NavigationDuplicated') throw error;
        })
}

export default router;