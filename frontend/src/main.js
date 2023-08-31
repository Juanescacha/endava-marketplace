import { createApp } from "vue";
import "./index.css";
import router from "./router/index";
import App from "./App.vue";

import("preline");

const app = createApp(App);

app.use(router).mount("#app");
