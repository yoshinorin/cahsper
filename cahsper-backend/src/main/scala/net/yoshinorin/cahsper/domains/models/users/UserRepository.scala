package net.yoshinorin.cahsper.domains.models.users

trait UserRepository {

  /**
   * Create user
   *
   * @param data Users Instance
   * @return created User
   */
  def insert(data: Users): Users

  /**
   * Find userName by Name
   *
   * @param userName
   * @return Users
   */
  def findByName(userName: UserName): Option[Users]

  /**
   * Get all users
   *
   * @return all users
   */
  def getAll: Seq[Users]

}
