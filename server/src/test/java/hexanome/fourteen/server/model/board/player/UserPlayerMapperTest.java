package hexanome.fourteen.server.model.board.player;

import hexanome.fourteen.server.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.BeforeAll;

@TestInstance(PER_CLASS)
public class UserPlayerMapperTest {

  static UserPlayerMapper userMapper;
  static User sampleUser;

  @BeforeAll
  public static void setUp() {
    userMapper = new UserPlayerMapper();
    sampleUser = new User();
  }

  @Test
  public void testNoArgsConstructor() {
    UserPlayerMapper mapper = new UserPlayerMapper();
    User user = new User();
    Player player = mapper.map(user);

    Assertions.assertEquals(player.uid(), user.name());
  }

  @Test
  public void testMap() {
    Player player = userMapper.map(sampleUser);

    Assertions.assertEquals(player.uid(), userMapper.map(sampleUser).uid());
  }
}