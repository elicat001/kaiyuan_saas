/**
 * 日志工具类
 * 统一管理日志输出，支持环境区分
 * 生产环境自动禁用 debug 和 info 级别日志
 */

// 日志级别
const LOG_LEVELS = {
  DEBUG: 0,
  INFO: 1,
  WARN: 2,
  ERROR: 3,
  NONE: 4
}

// 当前环境 - 从 uni-app 环境变量获取
const isDev = process.env.NODE_ENV === 'development'

// 当前日志级别 - 开发环境显示所有，生产环境只显示警告和错误
const currentLevel = isDev ? LOG_LEVELS.DEBUG : LOG_LEVELS.WARN

/**
 * 格式化日志消息
 * @param {string} level - 日志级别
 * @param {string} tag - 日志标签
 * @param {any[]} args - 日志参数
 */
const formatMessage = (level, tag, args) => {
  const timestamp = new Date().toISOString()
  const prefix = `[${timestamp}] [${level}]`
  if (tag) {
    return [prefix, `[${tag}]`, ...args]
  }
  return [prefix, ...args]
}

/**
 * 日志工具对象
 */
const logger = {
  /**
   * 调试日志 - 仅在开发环境输出
   * @param {string} tag - 日志标签（可选）
   * @param {...any} args - 日志内容
   */
  debug(tag, ...args) {
    if (currentLevel <= LOG_LEVELS.DEBUG) {
      console.log(...formatMessage('DEBUG', tag, args))
    }
  },

  /**
   * 信息日志 - 仅在开发环境输出
   * @param {string} tag - 日志标签（可选）
   * @param {...any} args - 日志内容
   */
  info(tag, ...args) {
    if (currentLevel <= LOG_LEVELS.INFO) {
      console.info(...formatMessage('INFO', tag, args))
    }
  },

  /**
   * 警告日志 - 始终输出
   * @param {string} tag - 日志标签（可选）
   * @param {...any} args - 日志内容
   */
  warn(tag, ...args) {
    if (currentLevel <= LOG_LEVELS.WARN) {
      console.warn(...formatMessage('WARN', tag, args))
    }
  },

  /**
   * 错误日志 - 始终输出
   * @param {string} tag - 日志标签（可选）
   * @param {...any} args - 日志内容
   */
  error(tag, ...args) {
    if (currentLevel <= LOG_LEVELS.ERROR) {
      console.error(...formatMessage('ERROR', tag, args))
    }
  },

  /**
   * 分组日志开始
   * @param {string} label - 分组标签
   */
  group(label) {
    if (currentLevel <= LOG_LEVELS.DEBUG && console.group) {
      console.group(label)
    }
  },

  /**
   * 分组日志结束
   */
  groupEnd() {
    if (currentLevel <= LOG_LEVELS.DEBUG && console.groupEnd) {
      console.groupEnd()
    }
  },

  /**
   * 表格日志
   * @param {any} data - 表格数据
   */
  table(data) {
    if (currentLevel <= LOG_LEVELS.DEBUG && console.table) {
      console.table(data)
    }
  },

  /**
   * 计时开始
   * @param {string} label - 计时标签
   */
  time(label) {
    if (currentLevel <= LOG_LEVELS.DEBUG && console.time) {
      console.time(label)
    }
  },

  /**
   * 计时结束
   * @param {string} label - 计时标签
   */
  timeEnd(label) {
    if (currentLevel <= LOG_LEVELS.DEBUG && console.timeEnd) {
      console.timeEnd(label)
    }
  },

  /**
   * API 请求日志
   * @param {string} url - 请求地址
   * @param {any} data - 请求数据
   */
  api(url, data) {
    if (currentLevel <= LOG_LEVELS.DEBUG) {
      console.log(...formatMessage('API', 'Request', [url, data]))
    }
  },

  /**
   * API 响应日志
   * @param {string} url - 请求地址
   * @param {any} response - 响应数据
   */
  apiResponse(url, response) {
    if (currentLevel <= LOG_LEVELS.DEBUG) {
      console.log(...formatMessage('API', 'Response', [url, response]))
    }
  },

  /**
   * 判断是否为开发环境
   * @returns {boolean}
   */
  isDev() {
    return isDev
  }
}

export default logger
export { logger, LOG_LEVELS }
