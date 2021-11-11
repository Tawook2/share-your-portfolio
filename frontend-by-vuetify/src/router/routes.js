import MainView from '@/views/MainView.vue';
import LoginView from '@/views/LoginView.vue';
import SignupView from '@/views/SignupView.vue';
import AccountView from '@/views/AccountView.vue';
import MessageView from '@/views/MessageView.vue';
import PostView from '@/views/PostView.vue';

const routes = [
    {
        path: '/',
        name: 'main',
        component: MainView,
    },
    {
        path: '/login',
        name: 'login',
        component: LoginView,
    },
    {
        path: '/signup',
        name: 'signup',
        component: SignupView,
    },
    {
        path: '/account',
        name: 'account',
        component: AccountView, 
    },
    {
        path: '/message',
        name: 'message',
        component: MessageView,
    },
    {
        path: '/post/:id',
        name: 'post',
        component: PostView,
    }
]

export default routes;