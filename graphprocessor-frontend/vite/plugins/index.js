import vue from '@vitejs/plugin-vue'

import createAutoImport from './auto-import'
import createSvgIcon from './svg-icon'
import createCompression from './compression'
import createSetupExtend from './setup-extend'
import monacoEditorPlugin from 'vite-plugin-monaco-editor'


export default function createVitePlugins(viteEnv, isBuild = false) {
    const vitePlugins = [vue()]
    vitePlugins.push(createAutoImport())
	vitePlugins.push(createSetupExtend())
    vitePlugins.push(createSvgIcon(isBuild))




    isBuild && vitePlugins.push(...createCompression(viteEnv))
    return vitePlugins
}

//    // 添加 monaco editor 插件
//     // vitePlugins.push(monacoEditorPlugin.default({
//     //     languageWorkers: ['editorWorkerService', 'typescript', 'json', 'html']
//     // }))