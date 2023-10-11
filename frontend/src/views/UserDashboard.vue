<script setup>
	import LinkListItem from "../components/Menus/LinkListItem.vue";
	import {
		ShoppingBagIcon,
		ListBulletIcon,
		CurrencyDollarIcon,
		UserIcon,
	} from "@heroicons/vue/24/outline";
</script>

<template>
	<main
		class="mt-20 min-w-[470px] lg:flex lg:h-[calc(100vh-4rem)] lg:gap-x-1 lg:rounded-br-xl lg:border lg:border-gray-100"
	>
		<nav class="bg-gray-100 lg:w-[30vw] lg:rounded-r-2xl lg:border-l-0">
			<ul class="mt-2">
				<li class="h-12">
					<link-list-item
						redirects-to="/users/me/profile"
						color-classes="hover:bg-endava-300 active:bg-endava-400 text-gray-800"
					>
						<UserIcon class="h-5 w-5 flex-none" />
						Profile
					</link-list-item>
				</li>
				<li class="h-12">
					<link-list-item
						redirects-to=""
						color-classes="hover:bg-endava-300 active:bg-endava-400 text-gray-800"
					>
						<ListBulletIcon
							class="h-5 w-5 flex-none text-current"
						/>
						Publications
					</link-list-item>
				</li>
				<li class="h-12">
					<link-list-item
						redirects-to="/users/me/sales-history"
						color-classes="hover:bg-endava-300 active:bg-endava-400 text-gray-800"
					>
						<CurrencyDollarIcon
							class="h-5 w-5 flex-none text-current"
						/>
						Sales history
					</link-list-item>
				</li>
				<li class="h-12">
					<link-list-item
						redirects-to="/users/me/purchase-history"
						color-classes="hover:bg-endava-300 active:bg-endava-400 text-gray-800"
					>
						<ShoppingBagIcon
							class="h-5 w-5 flex-none text-current"
						/>
						Purchase history
					</link-list-item>
				</li>
			</ul>
		</nav>
		<router-view></router-view>
	</main>
</template>
