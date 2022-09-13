let isToday = (t) =>
    new Date().toLocaleString().split(",")[0] ==
    t.toLocaleString().split(",")[0],
  isYesterday = (t) => {
    let e = new Date(),
      i = new Date();
    return (
      i.setDate(e.getDate() - 1),
      i.toLocaleString().split(",")[0] == t.toLocaleString().split(",")[0]
    );
  },
  formatDiscordTime = (t) => {
    let e = t.toLocaleString().split(",")[1];
    return isToday(t)
      ? "Today at" + e.substring(0, e.length - 6) + " " + e.slice(-2)
      : isYesterday(t)
      ? "Yesterday at" + e.substring(0, e.length - 6) + " " + e.slice(-2)
      : t.toLocaleString().split(",")[0];
  },
  isBlank = (t) =>
    Array.isArray(t)
      ? 0 === t.length
      : null === t || void 0 === t || 0 === t.replace(/\s/g, "").length;
