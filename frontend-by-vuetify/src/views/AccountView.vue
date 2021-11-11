<template>
    <v-layout class="justify-center">
        <v-sheet 
            width="600"
            elevation="1"
            class="pa-2"
        >
            <v-hover>
                <template v-slot:default="{ hover }">
                    <v-card elevation="0" class="py-10">
                        <div class="mx-auto" style="position: relative; width: 100px;">
                            <user-image 
                                v-if="!isLoading"
                                :id="user.id"
                                :size="100"
                            ></user-image>
                            <v-img
                                v-else
                                :src="require('@/assets/images/loading.gif')"
                            ></v-img>

                            <v-fade-transition>
                                <v-overlay
                                    v-if="hover"
                                    absolute
                                    class="rounded-lg"
                                >
                                    <v-btn outlined dark block class="mb-1" @click="inputFile()">수정</v-btn>
                                    <v-btn outlined dark block @click="removeImage()">삭제</v-btn>
                                
                                    <v-file-input 
                                        v-model="image.file"
                                        accept=".png"
                                        ref="file"
                                        style="display: none;"
                                        @change="openCropModal"
                                    />
                                </v-overlay>
                            </v-fade-transition>
                        </div>
                    </v-card>
                </template>
            </v-hover>

            <v-dialog v-model="showCropModal">
                <v-card>
                    <v-card-title>
                        Crop your profile picture
                    </v-card-title>
                
                    <v-card-text>
                        <cropper
                            :src="image.src"
                            :stencil-component="$options.components.CircleStencil"
                            :stencil-props="{ aspectRatio: 1/1 }"
                            @change="moveCooridate"
                            class="cropper"
                        ></cropper>
                    </v-card-text>
                
                    <v-card-actions>
                        <v-btn block color="primary" @click="uploadImage">Upload Image</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>

            <v-card elevation="0" width="300" class="mx-auto">
                <v-card-text>
                    <v-text-field
                        label="이메일"
                        :value="user.username"
                        readonly
                        class="mb-4"
                    ></v-text-field>

                    <v-dialog
                        v-model="dialog"
                        persistent
                        width="360"
                    >
                        <template v-slot:activator="{ on, attrs }">
                            <v-btn block elevation="0" color="secondary" v-on="on" :bind="attrs">
                                비밀번호 변경
                            </v-btn>
                        </template>
                        <v-card>
                            <v-card-title class="font-weight-bold text-h5 justify-center">
                                비밀번호 변경
                            </v-card-title>
                            <v-card-text>
                                <v-form ref="form">
                                    <v-text-field
                                        label="현재 비밀번호"
                                        type="password"
                                        v-model="passwordChangeForm.currentPassword"
                                        :rules="rules.currentPassword"
                                        autocomplete="new-password"
                                    ></v-text-field>
                                    <v-text-field
                                        label="새 비밀번호"
                                        type="password"
                                        v-model="passwordChangeForm.newPassword"
                                        :rules="rules.newPassword"
                                    ></v-text-field>
                                    <v-text-field
                                        label="새 비밀번호 확인"
                                        type="password"
                                        :rules="rules.newPasswordConfirmation"
                                    ></v-text-field>
                                </v-form>
                            </v-card-text>
                            <v-card-actions>
                                <v-spacer></v-spacer>
                                <v-btn color="error" @click="dialog = false">취소</v-btn>
                                <v-btn color="success" @click="submitChangePasswordForm">저장</v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-dialog>
                </v-card-text>
            </v-card>
        </v-sheet>
    </v-layout>
</template>

<script>
/* eslint-disable*/
import UserImage from '@/components/UserImage.vue';
import { Cropper, CircleStencil } from 'vue-advanced-cropper';
import 'vue-advanced-cropper/dist/style.css';
import { mapState } from 'vuex';
import AccountApi from '@/api/AccountApi.js';

function getMimeType(file, fallback = null) {
	const byteArray = (new Uint8Array(file)).subarray(0, 4);
    let header = '';
    for (let i = 0; i < byteArray.length; i++) {
       header += byteArray[i].toString(16);
    }
	switch (header) {
        case "89504e47":
            return "image/png";
        case "47494638":
            return "image/gif";
        case "ffd8ffe0":
        case "ffd8ffe1":
        case "ffd8ffe2":
        case "ffd8ffe3":
        case "ffd8ffe8":
            return "image/jpeg";
        default:
            return fallback;
    }
}

export default {
    name: 'AccountView',
    components: {
        UserImage,
        Cropper,
        CircleStencil,
    },
    data: function() {
        return {
           dialog: false,
           passwordChangeForm: {
               currentPassword: '',
               newPassword: '',
           },
           rules: {
               currentPassword: [
                   value => !!value || '현재 비밀번호를 입력해주세요.',
               ],
               newPassword: [
                   value => !!value || '새로운 비밀번호를 입력해주세요.',    
               ],
               newPasswordConfirmation: [
                   value => value === this.passwordChangeForm.newPassword || '비밀번호가 일치하지 않습니다.',
               ],
           },
           image: {
               src: null,
               type: null,
               file: null,
               coordinates: null,
           },
           showCropModal: false,
           isLoading: false,
        }
    },
    computed: {
        ...mapState({
            user: state => state.user.user,
        }),
    },
    methods: {
        validate() {
            this.$refs.form.validate();
        },
        submitChangePasswordForm() {
            this.validate();
        },
        inputFile() {
            this.$refs.file.$refs.input.click();
        },
        openCropModal(file) {
            if (file) {
                if (this.image.src) {
                    URL.revokeObjectURL(this.image.src);
                }

                const reader = new FileReader();
                const blob = URL.createObjectURL(file);

                reader.onload = (e) => {
                    this.image.src = blob;
                    this.image.type = getMimeType(e.target.result, file.type);
                    const type = this.image.type;
                    if (type !== 'image/png') {
                        alert('Only accept PNG!');
                        return;
                    }
                    
                    this.showCropModal = true;
                }

                reader.readAsArrayBuffer(file);
            }
        },
        moveCooridate({coordinates}) {
            this.image.coordinates = coordinates;
        },
        uploadImage() {
            this.showCropModal = false;
            this.isLoading = true;

            const form = new FormData();
            form.append('file', this.image.file);
            const coordinates = this.image.coordinates;
            Object.keys(coordinates).forEach(key => {
                form.append(key, coordinates[key]);
            });

            AccountApi.uploadImage(form)
                .finally(() => {
                    this.isLoading = false;
                });
        },
        removeImage() {
            this.isLoading = true;

            AccountApi.removeImage()
                .finally(() => {
                    this.isLoading = false;
                });
        }
    }
}
</script>