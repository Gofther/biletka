package biletka.main.dto.response.HallScheme;

public record SchemeSeat(
        boolean occupied,
        String number,
        String group,
        String position
) {
    public SchemeSeat(boolean occupied, String number, String group, String position) {
        this.occupied = occupied;
        this.number = number;
        this.group = group;
        this.position = position;
    }

    public SchemeSeat withOccupied(boolean newOccupied) {
        return new SchemeSeat(newOccupied, this.number, this.group, this.position);
    }
}
