<template>
    <v-layout class="justify-center">
        <v-sheet
            width="1000"
            elevation="1"
            class="pa-2"
        >

            <v-list 
                subheader
                dense
            >
                <v-subheader class="text-h6 px-4 my-4 font-weight-bold justify-space-between">
                    All Posts
                    <v-btn
                        v-if="isAuth"
                        color="red accent-2"
                        class="white--text"
                        elevation="0"
                        @click="openArticleCreateModal"
                    >게시물 등록</v-btn>
                    <v-dialog v-model="showArticleCreateModal" max-width="720">
                        <v-card>
                            <v-card-title class="text-h4 justify-center">
                                Flex Your PortFolio
                            </v-card-title>

                            <v-card-text>
                                <v-form
                                    ref="form"
                                    v-model="articleForm.valid"
                                >
                                    <v-text-field
                                        label="제목"
                                        v-model="articleForm.title"
                                        :rules="articleFormRules.title"
                                    ></v-text-field>

                                    <v-textarea
                                        label="내용"
                                        v-model="articleForm.content"
                                        :rules="articleFormRules.content"
                                        no-size
                                    ></v-textarea>

                                    <v-radio-group
                                        label="Category"
                                        v-model="articleForm.category"
                                        row
                                    >
                                        <v-radio
                                            v-for="(c, i) in categories" :key="i"
                                            :label="c.label"
                                            :value="c.value"
                                        ></v-radio>
                                    </v-radio-group>

                                    <v-file-input
                                        placeholder="pdf"
                                        v-model="articleForm.file"
                                        accept=".pdf"
                                        :rules="articleFormRules.file"
                                    ></v-file-input>
                                </v-form>
                            </v-card-text>

                            <v-card-actions class="justify-center">
                                <v-btn color="error" elevation="0" class="px-6" @click="closeArticleCreateModal">취소</v-btn>
                                <v-btn color="success" elevation="0" class="px-6" @click="submitArticleForm">등록</v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-dialog>
                </v-subheader>

                <v-list-item
                    v-for="(post, index) in posts"
                    :key="post.id"
                >
                    <v-list-item-avatar
                        tile
                        size="50%"
                    >
                        <v-img
                            width="100%"
                            :src="getPostImage(index)"
                            @click="moveToArticle(post.id)"
                            class="pointer"
                        ></v-img>
                    </v-list-item-avatar>
                    
                    <v-list-item-content 
                        class="align-start align-self-start"
                    >
                        <v-card flat>
                            <v-container class="d-flex px-2">
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
                                class="px-2 pointer" 
                                @click="moveToArticle(post.id)"
                            >
                                {{ post.title }}
                            </v-card-title>


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
                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </v-sheet>
    </v-layout>
</template>

<script>
import UserImage from '@/components/UserImage.vue';
import { mapState, mapGetters } from 'vuex';
import PostApi from '@/api/PostApi.js';

function dateFormat(time) {
    const date = new Date(time);
    return date;
}

export default {
    name: 'MainView',
    components: {
        UserImage,
    },
    data: function() {
        return {
           posts: [
                // {
                //     id: 1,
                //     imagePath : '',
                //     writer: 'test01',
                //     writerId: 1,
                //     writeDate: 'Dec 2, 2020',
                //     title: 'Low Maintenance Plants You Can Grow',
                //     content: 'Lorem ipsum, dolor sit amet consectetur adipisicing elit. Doloribus, odit?',
                //     viewCount: 11,
                //     commentCount: 5,
                //     likeCount: 3,
                //     isLiked: true,
                //     image: ''
                // },
                // {
                //     id: 3,
                //     imagePath : '',
                //     writer: 'test02',
                //     writerId: 2,
                //     writeDate: 'Dec 1, 2019',
                //     title: 'Healthy, Herby Meals',
                //     content: 'Lorem ipsum, dolor sit amet consectetur adipisicing elit. Doloribus, odit?',
                //     viewCount: 9,
                //     commentCount: 8,
                //     likeCount: 10,
                //     isLiked: false,
                //     image: '',
                // },
            ],
            showArticleCreateModal: false,
            categories: [
                {
                    label: 'IT',
                    value: 'it',
                },
                {
                    label: 'Design',
                    value: 'design',
                },
                {
                    label: 'Architect',
                    value: 'architect',
                },
                {
                    label: 'Marketing',
                    value: 'marketing',
                },
                {
                    label: 'Accounting',
                    value: 'accounting',
                },
            ],
            articleForm: {
                valid: false,
                title: '',
                content: '',
                category: 'it',
                file: null,
            },
            articleFormRules: {
                title: [
                    value => !!value || '제목을 입력해주세요.'
                ],
                content: [
                    value => !!value || '내용을 입력해주세요.'
                ],
                file: [
                    value => !!value || '파일을 등록해주세요.'
                ]
            }
        };
    },
    computed: {
        ...mapState({
            userId: state => state.user.user.id,
        }),
        ...mapGetters({
            isAuth: 'user/isAuth',
        }),
    },
    methods: {
        moveToArticle(postId) {
            this.$router.push(`/post/${postId}`);
        },
        moveToMessage(writerId, writer) {
            this.$router.push(`/message?id=${writerId}&name=${writer}`)
        },
        openArticleCreateModal() {
            this.showArticleCreateModal = true;
        },
        closeArticleCreateModal() {
            this.showArticleCreateModal = false;
        },
        validateArticleForm() {
            const valid =  this.$refs.form.validate();
            this.articleForm.valid = valid;
            return valid;
        },
        submitArticleForm() {
            if (!this.validateArticleForm()) {
                return;
            }

            this.closeArticleCreateModal();
            this.articleForm.writerId = this.userId;

            PostApi.savePost(this.articleForm)
                .then(response => {
                    console.log(response);
                })
                .catch(error => {
                    console.log(error);
                });
        },
        getPosts() {
            PostApi.fetchList()
                .then(response => {
                    const result = response.data;
                    this.posts = result;
                    for (const i in this.posts) {
                        this.posts[i].writeDate = dateFormat(this.posts[i].writeDate);
                    }
                    console.log(response);
                })
                .catch(error => {
                    console.log(error);
                })
        },
        getPostImage(index) {
            const post = this.posts[index];
            return `/api/images/${post.writerId}/${post.id}/0`;
        }
    },
    created: function() {
        this.getPosts();
    }
}
</script>