package results;

import models.GameSummary;

import java.util.List;

public record ListGamesResult(List<GameSummary> games) {
}
