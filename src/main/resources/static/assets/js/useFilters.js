export default function () {
    return {
        YYYYMMDD(value, format) {
            if (!value) {
              return undefined
            }
            if (!format) {
              format = 'YYYY.MM.DD'
            }
            return dayjs(value).format(format)
          },
    }
}