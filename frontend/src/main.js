import { createApp } from "vue";
import router from "./router";
import "./index.css";
import App from "./App.vue";

import("preline");

const app = createApp(App);

app.use(router).mount("#app");
