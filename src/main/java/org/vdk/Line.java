package org.vdk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vdk.util.Point2D;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Line <T extends Point2D> {
    private final T point1;
    private final T point2;
}
