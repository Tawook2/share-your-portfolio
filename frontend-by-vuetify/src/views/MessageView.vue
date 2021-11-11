<template>
    <v-layout class="justify-center align-center px-1">
        <v-sheet width="100%" max-width="1000px" class="rounded-lg">
            <v-row height="100%">
                <v-col cols="auto">
                    <v-list two-line color="grey lighten-3" class="rounded-l-lg pt-4" height="600" width="300">
                        <template v-for="(user, index) in users">
                            <v-list-item :key="user.id" @click="selectUser(user)">
                                <v-list-item-avatar>
                                    <user-image :id="user.id"></user-image>
                                </v-list-item-avatar>
                                
                                <v-list-item-content>
                                    <v-list-item-title class="d-flex justify-space-between">
                                        <span class="font-weight-bold">{{ user.name }}</span>
                                        <span class="font-weight-light grey--text">{{ user.latestSendTime }}</span>
                                    </v-list-item-title>
                                    <v-list-item-subtitle v-text="user.latestMessage"></v-list-item-subtitle>
                                </v-list-item-content>

                            </v-list-item>

                            <v-divider v-if="index < users.length-1" :key="index"></v-divider>
                        </template>
                    </v-list>

                </v-col>
                <v-col>
                    <v-card v-if="isSelected" flat class="rounded-r-lg pt-4">
                        <v-card-title>
                            <user-image class="mr-2" :id="selectedUser.id"></user-image>
                            <span>{{ selectedUser.name }}</span>
                        </v-card-title>

                        <v-divider class="mr-5"></v-divider>

                        <v-card-text>
                            <v-list class="overflow-y-auto" height="400">
                                <template v-for="(message) in messages">
                                    <v-list-item :key="message.messageNo" class="align-start" :class="{ 'flex-row-reverse': message.senderId === userId}">
                                        <span class="white--text cyan darken-1 px-2 py-1 rounded-xl mx-1" style="max-width: 200px;">{{ message.content }}</span>
                                        <span class="grey--text px-2 rounded">{{ toDateFormat(message.createDate) }}</span>
                                    
                                    </v-list-item>
                                </template>
                            </v-list>
                        </v-card-text>

                        <v-divider class="mr-5"></v-divider>

                        <v-card-actions class="py-0 pr-6 align-end">
                            <v-text-field v-model="text" @keyup.enter.native="sendMessage"></v-text-field>
                            <v-btn elevation="0" color="primary" class="ml-4 mr-1 mb-5 px-6" @click="sendMessage">Send</v-btn>
                        </v-card-actions>
                    </v-card>
                    <div v-else class="py-4 text-subtitle-1">대화 상대를 선택하세요</div>
                </v-col>
                
            </v-row>
        </v-sheet>
    </v-layout>
</template>

<script>
/* eslint-disable */
import UserImage from '@/components/UserImage.vue';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { mapState } from 'vuex';
import ChatApi from '@/api/ChatApi.js';

let socket = new SockJS('/api/ws');
let stompClient = Stomp.over(socket);

const isSameDay = (day1, day2) => {   
    return day1.getDate() === day2.getDate()
        && day1.getMonth() === day2.getMonth()
        && day1.getFullYear() === day2.getFullYear();
};


export default {
    name: 'MessageView',
    components: {
        UserImage,
    },
    data: function() {
        return {
            users: [
                // {
                //     id: 3,
                //     name: '테스터03',
                //     latestMessage: 'Hi',
                //     latestSendTime: '10:34 AM'
                // },
                // {
                //     id: 2,
                //     name: '테스터02',
                //     latestMessage: 'Hello!',
                //     latestSendTime: '11:30 PM'    
                // }
            ],
            selectedUser: null,
            text: '',
            messages: [
                // {
                //     senderId: 3,
                //     content: 'Hi',
                //     createDate: 1630223870000,
                // },
                // {
                //     senderId: 2,
                //     content: 'Hello',
                //     createDate: 1630223860000,
                // },
            ],
        }
    },
    computed: {
        ...mapState({
            userId: state => state.user.user.id,
        }),
        isSelected() {
            return !!this.selectedUser;
        }
    },
    methods: {
        selectUser(user) {
            this.selectedUser = user;
            ChatApi.getChatHistory(this.userId, user.id)
                .then(response => {
                    console.log(response);
                    this.messages = response.data;
                });
        },
        connectToChat() {
            stompClient.connect({}, (frame) => {
                stompClient.subscribe('/topic/messages/' + this.userId, (response) => {
                    let data = JSON.parse(response.body);
                    this.messages.push(data);
                });
            })            
        },
        sendMessage() {
            if (this.text.trim()) {
                const message = {
                    senderId: this.userId,
                    receiverId: this.selectedUser.id,
                    content: this.text,
                }
                stompClient.send(`/app/chat/${this.userId}/${this.selectedUser.id}`, {}, JSON.stringify(message));
                this.text = '';
            }
        },
        toDateFormat(timestamp) {
            const today = new Date();
            const date = new Date(timestamp);
            return isSameDay(date, today) 
                ? `${date.getHours()}:${date.getMinutes()}`
                : `${date.getMonth()}월${date.getDate()}일 ${date.getHours()}:${date.getMinutes()}`;
        }
    },
    created: function() {
        this.connectToChat();
        ChatApi.getChatUserList(this.userId)
            .then(response => {
                this.users = response.data;
            });
        const id = this.$route.query.id;
        const name = this.$route.query.name;
        if (id && name && id !== this.userId) {
            this.selectedUser = { id, name };
        }
    }
}
</script>\