<template>
    <v-layout class="justify-center">
        <v-card flat width="720" class="px-6 py-4">
            <v-container class="d-flex px-2 align-center">
                <v-menu bottom right rounded offset-y>
                    <template v-slot:activator="{ on }">
                        <v-btn icon v-on="on">
                            <user-image :id="post.writerId"></user-image>
                        </v-btn>
                    </template>
                    <v-card class="pa-1">
                        <v-btn text @click="moveToMessage(post.writerId, post.writer)">채팅하기</v-btn>
                    </v-card>
                </v-menu>
                <div class="mx-3 d-flex flex-column justify-space-between">
                    <span class="font-weight-bold">{{ post.writer }}</span>
                    <span class="font-weight-thin">{{ post.writeDate }}</span>
                </div>
            </v-container>

            <v-card-title 
                class="px-2" 
            >
                {{ post.title }}
            </v-card-title>

            <v-carousel height="auto" hide-delimiters>
                <v-carousel-item
                    v-for="i in post.numOfPage"
                    :key="i"
                    eager
                >
                    <v-img :src="getPostImage(i-1)" eager/>
                </v-carousel-item>
            </v-carousel>

            <v-card-text class="px-2">
                {{ post.content }}
            </v-card-text>

            <v-card-actions>
                <div class="mr-2">
                    <span>{{ post.viewCount }}</span> views
                </div>
                <div>
                    <span>{{ post.commentCount }}</span> comments
                </div>
                <v-spacer></v-spacer>
                <div>
                    <span>{{ post.likeCount }}</span>
                    <v-icon color="red" class="mx-2">
                        mdi-heart
                    </v-icon>
                </div>
            </v-card-actions>
        </v-card>
    </v-layout>
</template>

<script>
import UserImage from '@/components/UserImage.vue';
import PostApi from '@/api/PostApi.js';

function dateFormat(time) {
    const date = new Date(time);
    return date;
}

export default {
    name: 'PostView',
    components: {
        UserImage,
    },
    data: function() {
        return {
            post: {
                // id: 1,
                // imagePath : '',
                // writer: 'test01',
                // writerId: 1,
                // writeDate: 'Dec 2, 2020',
                // title: 'Low Maintenance Plants You Can Grow',
                // content: 'Lorem ipsum, dolor sit amet consectetur adipisicing elit. Doloribus, odit?',
                // viewCount: 11,
                // commentCount: 5,
                // likeCount: 3,
                // isLiked: true,
                // image: ''
                // numOfPage: 30
            },
        }
    },
    computed: {
        postId() {
            return this.$route.params.id;
        }
    },
    methods: {
        moveToMessage(writerId, writer) {
            this.$router.push(`/message?id=${writerId}&name=${writer}`)
        },
        getPost(id) {
            PostApi.fetchPost(id)
                .then(response => {
                    const result = response.data;
                    this.post = result;
                    this.post.writeDate = dateFormat(this.post.writeDate);
                })
                .catch(error => {
                    console.log(error);
                    this.$router.push('/');
                });
        },
        getPostImage(index) {
            return `/api/images/${this.post.writerId}/${this.post.id}/${index}`;
        }
    },
    created: function() {
        this.getPost(this.postId);
    }
}
</script>