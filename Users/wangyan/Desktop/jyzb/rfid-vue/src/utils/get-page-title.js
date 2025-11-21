import defaultSettings from '@/settings'

const title = defaultSettings.title || '徐州市警用装备管理平台'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
