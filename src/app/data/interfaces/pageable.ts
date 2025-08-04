export interface Pageable<T> {
  content: T[]
  number: number
  numberOfElements: number
  size: number
  first: boolean
  last: boolean
  empty: boolean
}
