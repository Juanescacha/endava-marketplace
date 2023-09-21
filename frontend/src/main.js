import { createApp } from "vue";
import router from "./router";
import "./index.css";
import App from "./App.vue";
import { createPinia } from "pinia";

import("preline");

const pinia = createPinia();
const app = createApp(App);

app.use(router);
app.use(pinia);

app.mount("#app");
