<script setup>
	import LinkListItem from "@/components/Menus/LinkListItem.vue";
	import {
		ShoppingBagIcon,
		ListBulletIcon,
		CurrencyDollarIcon,
		UserIcon,
	} from "@heroicons/vue/24/solid";
</script>

<template>
	<main class="min-w-[470px] lg:flex lg:min-h-[calc(100vh-5rem)] lg:gap-x-1">
		<nav class="border-b lg:w-[30vw] lg:border-l-0 lg:border-r">
			<ul class="space-y-1 p-4">
				<li class="h-12">
					<link-list-item
						redirects-to="/users/me/profile"
						color-classes="px-4 rounded-xl text-[1.125rem] font-medium hover:bg-gray-100 text-gray-900 group"
						:class="{
							'active pointer-events-none bg-gray-200':
								$route.path.includes('profile'),
						}"
					>
						<UserIcon
							class="h-5 w-5 flex-none text-gray-400 group-hover:text-gray-800 group-[.active]:text-gray-800"
						/>
						Profile
					</link-list-item>
				</li>
				<li class="h-12">
					<link-list-item
						redirects-to="/users/me/publications"
						color-classes="px-4 rounded-xl text-[1.125rem] font-medium hover:bg-gray-100 text-gray-900 group"
						:class="{
							'active pointer-events-none bg-gray-200':
								$route.path.includes('publications'),
						}"
					>
						<ListBulletIcon
							class="h-5 w-5 flex-none text-gray-400 group-hover:text-gray-800 group-[.active]:text-gray-800"
						/>
						Publications
					</link-list-item>
				</li>
				<li class="h-12">
					<link-list-item
						redirects-to="/users/me/sales-history"
						color-classes="px-4 rounded-xl text-[1.125rem] font-medium hover:bg-gray-100 text-gray-900 group"
						:class="{
							'active pointer-events-none bg-gray-200':
								$route.path.includes('sales-history'),
						}"
					>
						<CurrencyDollarIcon
							class="h-5 w-5 flex-none text-gray-400 group-hover:text-gray-800 group-[.active]:text-gray-800"
						/>
						Sales
					</link-list-item>
				</li>
				<li class="h-12">
					<link-list-item
						redirects-to="/users/me/purchase-history"
						color-classes="px-4 rounded-xl text-[1.125rem] font-medium hover:bg-gray-100 text-gray-900 group"
						:class="{
							'active pointer-events-none bg-gray-200':
								$route.path.includes('purchase-history'),
						}"
					>
						<ShoppingBagIcon
							class="h-5 w-5 flex-none text-gray-400 group-hover:text-gray-800 group-[.active]:text-gray-800"
						/>
						Purchases
					</link-list-item>
				</li>
			</ul>
		</nav>
		<router-view></router-view>
	</main>
</template>
